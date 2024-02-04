package com.example.demo.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.entities.Users;
import com.example.demo.services.UsersService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;

import jakarta.servlet.http.HttpSession;


@Controller
public class PaymentController {
	
	@Autowired
	UsersService service;
	
	@GetMapping("/makePayment")
	public String makePayment() {
		return "makePayment";
	}
	@GetMapping("/payment-success")
	public String paymentSuccess(HttpSession session) {
		String mail =  (String) session.getAttribute("email");
		Users u = service.getUser(mail);
		u.setPremium(true);
		service.updateUser(u);
		return "login";
	}
	
	@GetMapping("/payment-failure")
	public String paymentFailure() {
		return "customerHome";
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
	

	
	
	@PostMapping("/verify")
	@ResponseBody
	public boolean verifyPayment(@RequestParam  String orderId, @RequestParam String paymentId, @RequestParam String signature) {
	    try {
	        // Initialize Razorpay client with your API key and secret
	        RazorpayClient razorpayClient = new RazorpayClient("rzp_test_GQesAPyg7iVbbz", "noQsixP6LjHxl9LmRCiSm8ux");
	        // Create a signature verification data string
	        String verificationData = orderId + "|" + paymentId;

	        // Use Razorpay's utility function to verify the signature
	        boolean isValidSignature = Utils.verifySignature(verificationData, signature, "noQsixP6LjHxl9LmRCiSm8ux");

	        return isValidSignature;
	    } catch (RazorpayException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	
}
