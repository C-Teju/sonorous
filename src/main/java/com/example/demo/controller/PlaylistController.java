package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entities.Playlist;
import com.example.demo.entities.Song;
import com.example.demo.entities.Users;
import com.example.demo.services.PlaylistService;
import com.example.demo.services.SongService;
import com.example.demo.services.UsersService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class PlaylistController {
	@Autowired
	SongService songService;
	
	@Autowired
	PlaylistService playlistService;
	
	@Autowired
	UsersService service;
	
	@GetMapping("/createPlaylist")
	public String createPlaylist(Model model, HttpSession session) {
		String email=(String)session.getAttribute("email");
		
		List<Song> songList=songService.findAllSongs();
		model.addAttribute("songs",songList);
		
		  String role = service.getRole(email);
		    model.addAttribute("userRole", role);
		
		return "createPlaylist";
	}
	
	@PostMapping("/addPlaylist")
	public String addPlaylist(@ModelAttribute Playlist playlist,HttpSession session,Model model ) {
		String email=(String)session.getAttribute("email");
		
		playlistService.addPlaylist(playlist);
		List<Song> songList=playlist.getSongs();
//		updating song table
		for(Song s: songList) {
			s.getPlaylists().add(playlist);
//			update in db
			songService.updateSong(s);
		}
		String role=service.getRole(email);
		if(role.equals("admin")) {
			return "adminHome";
			
		}
		else {
			Users user=service.getUser(email);
			boolean userStatus=user.isPremium();
			
			List<Song> songsList=songService.findAllSongs();
			model.addAttribute("songs",songsList);

			model.addAttribute("isPremium", userStatus);
			return "customerHome";
		}
		
		
	}
	
	@GetMapping("/viewPlaylists")
	public String viewPlaylists(Model model, HttpSession session) {
	    String email = (String) session.getAttribute("email");

	    if (email == null) {
	        
	        return "login";
	    }
	    Users user = service.getUser(email);
	    
	    if (user == null) {
	        
	        return "login";
	    }
	    boolean userStatus = user.isPremium();

	    String role = service.getRole(email);
	    model.addAttribute("userRole", role);

	    List<Playlist> playlistList = playlistService.fetchAllPlaylists();
	    model.addAttribute("playlistList", playlistList);

	    model.addAttribute("username", user.getUsername());
	    model.addAttribute("isPremium", userStatus);
	    
	    return "displayPlaylists";
	}
	
	
}

