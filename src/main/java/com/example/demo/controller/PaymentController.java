package com.example.demo.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entities.Users;
import com.example.demo.services.UsersService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import jakarta.servlet.http.HttpSession;






@Controller
public class PaymentController {
	
	@Autowired
	UsersService service;
	
	@GetMapping("/makePayment")
	public String makePayment() {
		return "makePayment";
	}
	
	
	@SuppressWarnings("finally")
	@PostMapping("/createOrder")
	@ResponseBody
	public String createOrder(HttpSession session) {

		int  amount  = 799;
		Order order=null;
		try {
			RazorpayClient razorpay=new RazorpayClient("rzp_test_GQesAPyg7iVbbz", "noQsixP6LjHxl9LmRCiSm8ux");

			JSONObject orderRequest = new JSONObject();
			orderRequest.put("amount", amount*100); // amount in the smallest currency unit i.e in paise so it's being converted.
			orderRequest.put("currency", "INR");
			orderRequest.put("receipt", "order_rcptid_11");

			order = razorpay.orders.create(orderRequest);
// Above this (from @suppressWarnings)is the copied code, below is(until updateUser ) what we have written to customize.
			String mail =  (String) session.getAttribute("email");

			Users u = service.getUser(mail);
			u.setPremium(true);
			service.updateUser(u);

		} catch (RazorpayException e) {
			e.printStackTrace();
		}
		finally {
			return order.toString();
		}
	}
	
//	@GetMapping("/makePayment")
//	public String makePayment2(@RequestParam("email") String email) {
//		
//		Users u = service.getUser(email);
//		if(u.isPremium()==true) {
//			return "displaySongs";
//		}
//		
//		else {
//			return "customerHome";
//		}
//		
//	}
	
	
	
}
