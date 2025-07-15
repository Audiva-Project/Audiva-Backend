package com.example.audiva.controller;

import com.example.audiva.dto.request.ApiResponse;
import com.example.audiva.dto.request.SongRequest;
import com.example.audiva.dto.response.SongResponse;
import com.example.audiva.entity.Song;
import com.example.audiva.entity.User;
import com.example.audiva.mapper.SongMapper;
import com.example.audiva.repository.UserRepository;
import com.example.audiva.service.SongService;
import com.example.audiva.service.UserService;
import org.springframework.core.io.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/songs")
public class SongController {
    @Autowired
    private SongService songService;

    @Autowired
    private SongMapper songMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

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
    public ResponseEntity<Resource> downloadSong(
            @PathVariable Long id,
            @RequestParam(defaultValue = "128kbps") String quality) {

        // 1) Lấy song entity
        Song song = songService.getSongEntityById(id);
        if (song == null) {
            return ResponseEntity.notFound().build();
        }

        // 2) Xác thực user
        Authentication curUser = SecurityContextHolder.getContext().getAuthentication();
        String username = curUser.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean isPremium = userService.isPremium(user);
        int quota = isPremium ? 100 : 50;

        if (user.getDownloadCount() != null && user.getDownloadCount() >= quota) {
            return ResponseEntity.status(403).build();
        }

        userService.increaseDownloadCount(user);

        // 3) Tính tên file theo bitrate
        String filename = song.getAudioUrl();
        String finalFilename = "128kbps".equals(quality) ? filename : quality + "_" + filename;

        // 4) Build đường dẫn tuyệt đối
        Path filePath = Paths.get(System.getProperty("user.dir"), "uploads", "mp3", finalFilename).normalize();

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        try {
            Resource resource = new UrlResource(filePath.toUri());
            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .contentLength(Files.size(filePath))
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)  // Hoặc trả về chuẩn audio/mpeg nếu chắc chắn là mp3
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
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

}
