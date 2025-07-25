package com.example.audiva.controller;

import com.example.audiva.dto.request.ArtistRequest;
import com.example.audiva.dto.response.ArtistResponse;
import com.example.audiva.service.ArtistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {
    @Autowired
    private ArtistService artistService;

    @GetMapping
    public List<ArtistResponse> getAllArtists() {
        return artistService.findAllArtists();
    }

    @GetMapping("/{id}")
    public ArtistResponse getArtistById(@PathVariable Long id) {
        return artistService.findArtistById(id);
    }

    @GetMapping("/search/{name}")
    public List<ArtistResponse> searchArtistsByName(@PathVariable String name) {
        return artistService.searchArtistsByName(name);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArtistResponse createArtist(@Valid @ModelAttribute ArtistRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        return artistService.createArtist(request, file);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ArtistResponse updateArtist(@PathVariable Long id, @ModelAttribute ArtistRequest request) throws IOException {
        return artistService.updateArtist(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteArtist(@PathVariable Long id) {
        artistService.deleteArtist(id);
    }
}
