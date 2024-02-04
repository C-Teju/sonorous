package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.entities.Song;
import com.example.demo.services.SongService;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SongController {
	@Autowired
	SongService service;

	@PostMapping("/addSong")
	public String addSong(@ModelAttribute Song song) {
		boolean songStatus = service.songExists(song.getName());
		if (songStatus == false) {
			service.addSong(song);
			System.out.println("Song added successfully !");

		} else {
			System.out.println("Song already exists");
		}
		return "adminHome";
	}

	@GetMapping("/viewSongs")
	public String viewSongs(Model model) {
		List<Song> songList = service.findAllSongs();
		model.addAttribute("songs", songList);
		return "displaySongs";
	}
	
	@GetMapping("/playSongs")
	public String playSongs(Model model) {
		boolean priemiumUser = false;
		if (priemiumUser == true) {
			List<Song> songList = service.findAllSongs();
			model.addAttribute("songs", songList);
			return "displaySongs";
		}else {
			return "makePayment";
		}
		
	}
	
		@GetMapping("/searchSongs")
		public String searchSongs(@RequestParam( name="query",required=false) String query, Model model) {
			if(query!=null && !query.isEmpty()) {
				List<Song> searchResults=service.searchSongs(query);
				model.addAttribute("songs",searchResults);
			}else {
				List<Song> allSongs=service.findAllSongs();
				  
			        if (allSongs.isEmpty()) {
			            model.addAttribute("message", "No songs found.");
			        } else {
			            model.addAttribute("songs", allSongs);
			        }
			}
			
			return "displaySongs";
		}
		
		
		 @GetMapping("/adminHome")
		    public String adminHome() {
		        return "adminHome";
		    }
		 @GetMapping("/addSong")
		 public String addSong() {
		 	return "newSong";
		 }
		 
		 
		
		
}
