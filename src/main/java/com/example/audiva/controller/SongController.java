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
import java.nio.file.Paths;

@RestController
@RequestMapping("api/songs")
public class SongController {
    @Autowired
    private SongService songService;

    @Autowired
    private SongMapper songMapper;

    @GetMapping
    public Page<SongResponse> getAllSongs(
            @PageableDefault(size = 8, page = 0) Pageable pageable
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
            , @Valid @ModelAttribute SongRequest song) {
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

        String audioStoragePath = Paths.get("uploads/mp3").toAbsolutePath().toString() + File.separator;
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

    // increase play count
    @PostMapping("/{id}/play")
    public ApiResponse<Void> increasePlayCount(@PathVariable Long id) {
        songService.increasePlayCount(id);
        return ApiResponse.<Void>builder()
                .message("Play count increased successfully")
                .build();
    }

    @GetMapping("/{id}/lyrics")
    public ResponseEntity<String> getLyricsBySongId(@PathVariable Long id) {
        return songService.getLyricsBySongId(id)
                .map(lyrics -> ResponseEntity.ok(lyrics.getContent()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/lyrics")
    public ApiResponse<String> updateLyrics(
            @PathVariable Long id,
            @RequestBody String newLyricsContent) {

        songService.updateLyrics(id, newLyricsContent);

        return ApiResponse.<String>builder()
                .result("Lyrics updated successfully")
                .build();
    }

    // fetch songs by artist id
    @GetMapping("/artist/{artistId}")
    public ApiResponse<Page<SongResponse>> getSongsByArtistId(
            @PathVariable Long artistId,
            @PageableDefault(size = 10) Pageable pageable) {
        return ApiResponse.<Page<SongResponse>>builder()
                .result(songService.getSongsByArtistId(artistId, pageable))
                .build();
    }

}
