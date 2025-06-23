package com.example.audiva.controller;

import com.example.audiva.dto.request.ArtistRequest;
import com.example.audiva.entity.Artist;
import com.example.audiva.service.ArtistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/artists")
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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Artist createArtist(@Valid @ModelAttribute ArtistRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        return artistService.createArtist(request, file);
    }

    @PutMapping("/{id}")
    public Artist updateArtist(@PathVariable Long id, @Valid @RequestBody ArtistRequest request) {
        return artistService.updateArtist(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
    }
}
