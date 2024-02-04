package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.demo.entities.LoginData;
import com.example.demo.entities.Playlist;
import com.example.demo.entities.Song;
import com.example.demo.entities.Users;
import com.example.demo.services.PlaylistService;
import com.example.demo.services.SongService;
import com.example.demo.services.UsersService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class UsersController {
	@Autowired
	UsersService service;
	@Autowired
	SongService songService;
	@Autowired
	PlaylistService playlistService;
	@PostMapping("/register")
	public String addUsers(@ModelAttribute Users user) {
		boolean userStatus= service.emailExists(user.getEmail());
		if(userStatus ==false) {
			service.addUser(user);
			System.out.println("User Added");
		}else {
			System.out.println("User already exists !");
		}
		
		return "login";
	}
	@PostMapping("/validate")
	public String validate(@RequestParam ("email") String email ,@RequestParam 
		("password") String password,  
			HttpSession session,Model model) {
		
		Users user = service.getUserByEmailAndPassword(email, password);
		
		if(service.validateUser(email,password)==true) {
			String role=service.getRole(email);
			session.setAttribute("email", email);
			if(role.equals("admin")) {
				return "adminHome";
			}else {
				
				boolean userStatus=user.isPremium();
				
				List<Song> songsList=songService.findAllSongs();
				model.addAttribute("songs",songsList);
				
				model.addAttribute("username", user.getUsername());
				
				model.addAttribute("isPremium", userStatus);
				return "customerHome";
			}
			
		}
		else {
			
			model.addAttribute("loginError", true);
			return "login";
			
		}
	}
	
	@GetMapping("/customerHome")
    public String customerHome(HttpSession session, Model model) {
		 String email = (String) session.getAttribute("email");
		    
		    if (email == null) {
		        
		        return "login";
		    }

		    Users user = service.getUser(email);
		    
		    if (user == null) {
		        
		        return "login";
		    }

		    boolean userStatus = user.isPremium();

		    List<Song> songsList = songService.findAllSongs();
		    model.addAttribute("songs", songsList);

		    model.addAttribute("username", user.getUsername());
		    model.addAttribute("isPremium", userStatus);

        return "customerHome";
	
	}
	
	
	

	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "login";
	}
}
