package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entities.Song;

public interface SongRepository extends JpaRepository<Song, Integer>{

	public Song findByName(String name);
	@Query("SELECT s FROM Song s WHERE (LOWER(s.name) || LOWER(s.artist)|| LOWER(s.genre)) LIKE LOWER(concat('%', :query, '%'))")
	public List<Song> searchSongs(@Param("query") String query);
	
	

}
