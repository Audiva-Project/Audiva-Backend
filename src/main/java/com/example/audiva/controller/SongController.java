package com.example.audiva.controller;


import com.example.audiva.dto.request.ApiResponse;
import com.example.audiva.dto.request.SongRequest;
import com.example.audiva.dto.response.SongResponse;
import com.example.audiva.entity.Song;
import com.example.audiva.mapper.SongMapper;
import com.example.audiva.service.SongService;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
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
    public Page<SongResponse> getAllSongs(
            @PageableDefault(size = 4, page = 0) Pageable pageable
    ) {
        return songService.getAllSong(pageable);
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

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadSong(@PathVariable Long id) {
        Song song = songService.getSongEntityById(id);
        if (song == null) {
            return ResponseEntity.notFound().build();
        }

        String audioStoragePath = "D:/Audiva-Backend/uploads/mp3/";
        String filePath = audioStoragePath + song.getAudioUrl();
        File file = new File(filePath);
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        FileSystemResource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getName() + "\"")
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public void deleteSong(@PathVariable Long id) {
        songService.deleteSong(id);
    }

    // get all songs create by me
    @GetMapping("/created-by-me")
    public ApiResponse<Page<SongResponse>> getSongsCreatedByMe(
            @PageableDefault(size = 10) Pageable pageable) {
        return ApiResponse.<Page<SongResponse>>builder()
                .result(songService.getSongsCreatedByMe(pageable))
                .build();
    }
}
