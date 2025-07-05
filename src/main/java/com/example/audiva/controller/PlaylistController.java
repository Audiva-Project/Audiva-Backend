package com.example.audiva.controller;

import com.example.audiva.dto.request.ApiResponse;
import com.example.audiva.dto.request.PlaylistRequest;
import com.example.audiva.dto.response.PlaylistResponse;
import com.example.audiva.dto.response.SongResponse;
import com.example.audiva.entity.User;
import com.example.audiva.repository.UserRepository;
import com.example.audiva.service.PlaylistService;
import com.example.audiva.service.StorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/playlists")
@RequiredArgsConstructor
public class PlaylistController {
    @Autowired
    PlaylistService playlistService;

    @Autowired
    StorageService fileStorageService;

    @Autowired
    UserRepository userRepository;

    @GetMapping
    public List<PlaylistResponse> getMyPlaylists() {
        return playlistService.getMyPlaylists();
    }

    @GetMapping("/{playlistId}/songs")
    public List<SongResponse> getSongsInPlaylist(@PathVariable Long playlistId) {
        return playlistService.getSongsInPlaylist(playlistId);
    }

    @PostMapping("/{playlistId}/add/{songId}")
    public ApiResponse<PlaylistResponse> addSongToPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId
    ) {
        Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Gọi service
        PlaylistResponse playlistResponse = playlistService.addSongToPlaylist(
                playlistId,
                songId,
                user.getId() // Lấy UUID từ entity User
        );

        // Trả về kết quả
        return ApiResponse.<PlaylistResponse>builder()
                .result(playlistResponse)
                .code(0)
                .message("Add song to playlist success")
                .build();
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PlaylistResponse> createPlaylist(@ModelAttribute @Valid PlaylistRequest request) throws IOException {
        return ApiResponse.<PlaylistResponse>builder()
                .result(playlistService.createPlaylist(request))
                .build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PlaylistResponse> updatePlaylist(
            @PathVariable Long id,
            @ModelAttribute PlaylistRequest request) {
        return ApiResponse.<PlaylistResponse>builder()
                .result(playlistService.updatePlaylist(id, request))
                .build();
    }

    @DeleteMapping("/{playlistId}/remove/{songId}")
    public ApiResponse<PlaylistResponse> deleteSongFromPlaylist(
            @PathVariable Long playlistId,
            @PathVariable Long songId
    ) {
        Authentication authentication = (Authentication) SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PlaylistResponse playlistResponse = playlistService.deleteSongFromPlaylist(
                playlistId,
                songId,
                user.getId()
        );
        return ApiResponse.<PlaylistResponse>builder()
                .result(playlistResponse)
                .code(0)
                .message("Delete song from playlist success")
                .build();
    }

    @DeleteMapping("/{playlistId}")
    public ApiResponse<Void> deletePlaylist(@PathVariable Long playlistId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        playlistService.deletePlaylist(playlistId, user.getId());

        return ApiResponse.<Void>builder()
                .code(0)
                .message("Delete playlist success")
                .build();
    }


//    // 👉 Endpoint test: thêm bài hát ID 1 vào playlist ID 1
//    @GetMapping("/test-add")
//    public PlaylistResponse testAdd() {
//        Long playlistId = 1L; // giả sử playlist ID 1
//        Long songId = 1L;     // giả sử song ID 1
//        return playlistService.addSongToPlaylist(playlistId, n);
//    }

}
