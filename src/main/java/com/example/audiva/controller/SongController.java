package com.example.audiva.controller;


import com.example.audiva.entity.Song;
import com.example.audiva.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/songs")
public class SongController {
    @Autowired
    private SongService songService;

    @GetMapping
    public List<Song> getAllSongs() {
        return songService.getAllSong();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById (@PathVariable Long id) {
        return ResponseEntity.ok(songService.getSongById(id));
    }

    @PostMapping("/{id}")
    public ResponseEntity<Song> createSong (@RequestBody Song song) {
        return ResponseEntity.ok(songService.createSong(song));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Song> updateSong (@PathVariable Long id
            , @RequestBody Song song) {
        return ResponseEntity.ok(songService.updateSong(id, song));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong (@PathVariable Long id) {
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }
}
