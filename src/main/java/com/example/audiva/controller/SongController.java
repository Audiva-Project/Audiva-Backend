package com.example.audiva.controller;


import com.example.audiva.dto.request.SongRequest;
import com.example.audiva.dto.response.SongResponse;
import com.example.audiva.entity.Song;
import com.example.audiva.mapper.SongMapper;
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

    @Autowired
    private SongMapper songMapper;

    @GetMapping
    public List<Song> getAllSongs() {
        return songService.getAllSong();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById (@PathVariable Long id) {
        return ResponseEntity.ok(songService.getSongById(id));
    }

    @PostMapping
    public ResponseEntity<SongResponse> createSong (@RequestBody SongRequest song) {
        return ResponseEntity.ok(songMapper.toSongResponse(songService.createSong(song)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SongResponse> updateSong (@PathVariable Long id
            , @RequestBody SongRequest song) {
        Song updatedSong = songService.updateSong(id, song);
        return ResponseEntity.ok(songMapper.toSongResponse(updatedSong));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong (@PathVariable Long id) {
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }
}
