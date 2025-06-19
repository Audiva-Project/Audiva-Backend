package com.example.audiva.controller;

import com.example.audiva.entity.Album;
import com.example.audiva.entity.Genre;
import com.example.audiva.entity.Song;
import com.example.audiva.service.MusicCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/music")
public class MusicCatalogController {
    @Autowired
    private MusicCatalogService musicCatalogService;

    //Song endpoint
    @PostMapping("/songs")
    public ResponseEntity<Song> addSong (@RequestBody Song song) {
        return ResponseEntity.ok(musicCatalogService.addSong(song));
    }

    @GetMapping("songs/artist/{artist}")
    public ResponseEntity<List<Song>> getSongByArtist (@PathVariable String artist) {
        return ResponseEntity.ok(musicCatalogService.getSongsByArtist(artist));
    }

    @GetMapping("songs/genre/{genres}")
    public ResponseEntity<List<Song>> getSongByGenre (@PathVariable String genre) {
        return ResponseEntity.ok(musicCatalogService.getSongsByGenre(genre));
    }

    // Album endpoints
    @PostMapping("/albums")
    public ResponseEntity<Album> addAlbum(@RequestBody Album album) {
        return ResponseEntity.ok(musicCatalogService.addAlbum(album));
    }

    @GetMapping("/albums/artist/{artist}")
    public ResponseEntity<List<Album>> getAlbumsByArtist(@PathVariable String artist) {
        return ResponseEntity.ok(musicCatalogService.getAlbumsByArtist(artist));
    }

    // Genre endpoints
    @PostMapping("/genres")
    public ResponseEntity<Genre> addGenre(@RequestBody Genre genre) {
        return ResponseEntity.ok(musicCatalogService.addGenre(genre));
    }

    @GetMapping("/genres/{name}")
    public ResponseEntity<Genre> getGenreByName(@PathVariable String name) {
        return ResponseEntity.ok(musicCatalogService.getGenreByName(name));
    }

}
