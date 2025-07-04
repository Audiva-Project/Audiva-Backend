package com.example.audiva.controller;

import com.example.audiva.dto.request.AlbumRequest;
import com.example.audiva.dto.request.ApiResponse;
import com.example.audiva.dto.response.AlbumResponse;
import com.example.audiva.entity.Album;
import com.example.audiva.entity.Song;
import com.example.audiva.exception.AppException;
import com.example.audiva.exception.ErrorCode;
import com.example.audiva.repository.AlbumRepository;
import com.example.audiva.repository.SongRepository;
import com.example.audiva.service.AlbumService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/albums")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService albumService;
    private final SongRepository songRepository;
    private final AlbumRepository albumRepository;

    @GetMapping
    public List<AlbumResponse> getAllAlbums() {
        return albumService.getAllAlbums();
    }

    @GetMapping("/{id}")
    public ApiResponse<AlbumResponse> getAlbumById(@PathVariable Long id) {
        return ApiResponse.<AlbumResponse>builder()
                .result(albumService.getAlbumById(id))
                .build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<AlbumResponse> createAlbum(@ModelAttribute @Valid AlbumRequest request) throws IOException {
        return ApiResponse.<AlbumResponse>builder()
                .result(albumService.createAlbum(request))
                .build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<AlbumResponse> updateAlbum(@PathVariable Long id,
                                                  @Valid @ModelAttribute  AlbumRequest request) {
        return ApiResponse.<AlbumResponse>builder()
                .result(albumService.updateAlbum(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    public void deleteAlbum(@PathVariable Long id) {
        albumService.deleteAlbum(id);
    }

    @DeleteMapping("/{albumId}/songs/{songId}")
    public ResponseEntity<?> removeSongFromAlbum(@PathVariable Long albumId, @PathVariable Long songId) {
        Album album = albumRepository.findById(albumId)
                .orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new AppException(ErrorCode.SONG_NOT_FOUND));

        if (song.getAlbum() == null || !song.getAlbum().getId().equals(albumId)) {
            throw new AppException(ErrorCode.SONG_NOT_IN_ALBUM);
        }

        // Bước 4: Xoá liên kết album
        song.setAlbum(null);
        songRepository.save(song);

        return ResponseEntity.ok("Removed song from album successfully");
    }
}

