package com.example.audiva.controller;


import com.example.audiva.dto.request.ApiResponse;
import com.example.audiva.dto.request.SongRequest;
import com.example.audiva.dto.response.SongResponse;
import com.example.audiva.entity.Song;
import com.example.audiva.mapper.SongMapper;
import com.example.audiva.service.SongService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/songs")
public class SongController {
    @Autowired
    private SongService songService;

    @Autowired
    private SongMapper songMapper;

    @GetMapping
    public List<SongResponse> getAllSongs() {
        return songService.getAllSong();
    }

    @GetMapping("/{id}")
    public ApiResponse<SongResponse> getSongById(@PathVariable Long id) {
        return ApiResponse.<SongResponse>builder()
                .result(songService.getSongById(id)).build();
    }

    @PostMapping(value = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<SongResponse> createSong(@ModelAttribute SongRequest song) throws IOException {
        return ApiResponse.<SongResponse>builder()
                .result((songService.createSong(song)))
                .build();
    }

    @PutMapping(value = "/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<SongResponse> updateSong(@PathVariable Long id
            ,@Valid @ModelAttribute SongRequest song) {
        return ApiResponse.<SongResponse>builder()
                .result(songService.updateSong(id, song))
                .build();
    }

    @DeleteMapping("/{id}")
    public void deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
    }
}
