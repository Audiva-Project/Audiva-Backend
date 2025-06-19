package com.example.audiva.controller;

import com.example.audiva.dto.request.ArtistRequest;
import com.example.audiva.entity.Artist;
import com.example.audiva.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {
    @Autowired
    private ArtistService artistService;

    @GetMapping
    public List<Artist> getAllArtists() {
        return artistService.findAllArtists();
    }

    @GetMapping("/{id}")
    public Artist getArtistById(@PathVariable Long id) {
        return artistService.findArtistById(id);
    }

    @GetMapping("/search/{name}")
    public List<Artist> searchArtistsByName(@PathVariable String name) {
        return artistService.searchArtistsByName(name);
    }

    @PostMapping
    public Artist createArtist(@RequestBody ArtistRequest request) {
        return artistService.createArtist(request);
    }

    @PutMapping("/{id}")
    public Artist updateArtist(@PathVariable Long id, @RequestBody ArtistRequest request) {
        return artistService.updateArtist(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
    }
}
