package com.example.audiva.service;

import com.example.audiva.dto.request.PlaylistRequest;
import com.example.audiva.dto.response.PlaylistResponse;
import com.example.audiva.dto.response.SongResponse;
import com.example.audiva.entity.*;
import com.example.audiva.exception.AppException;
import com.example.audiva.exception.ErrorCode;
import com.example.audiva.mapper.PlaylistMapper;
import com.example.audiva.mapper.SongMapper; // Import SongMapper
import com.example.audiva.repository.PlayListRepository;
import com.example.audiva.repository.PlaylistSongRepository;
import com.example.audiva.repository.SongRepository;
import com.example.audiva.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PlaylistService {

    PlayListRepository playlistRepository;
    PlaylistSongRepository playlistSongRepository;
    SongRepository songRepository;
    PlaylistMapper playlistMapper;
    UserRepository userRepository;
    StorageService storageService;
    SongMapper songMapper; // Inject SongMapper

    public PlaylistResponse createPlaylist(PlaylistRequest request) {
        User user = getCurrentUser();

        MultipartFile thumbnailFile = request.getThumbnailUrl();

        String fileUrl;
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            try {
                fileUrl = storageService.uploadFile(thumbnailFile);
            } catch (IOException e) {
                // Nên có logging ở đây
                throw new RuntimeException("Failed to upload thumbnail", e);
            }
        } else {
            fileUrl = "1751428697154_playlist.jpg"; // Giá trị mặc định
        }

        Playlist playlist = new Playlist();
        playlist.setName(request.getName());
        playlist.setDescription(request.getDescription());
        playlist.setThumbnailUrl(fileUrl);
        playlist.setUser(user);
        // playlist.setCreatedAt(LocalDateTime.now()); // JPA @CreatedDate sẽ tự động quản lý

        Playlist saved = playlistRepository.save(playlist);
        return playlistMapper.toPlaylistResponse(saved);
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username)); // Thêm thông tin để debug dễ hơn
    }

    public List<PlaylistResponse> getMyPlaylists() {
        User currentUser = getCurrentUser();
        // findAllByUserId có thể cần kiểu Long cho userId nếu ID là Long
        // Kiểm tra lại User entity và repository để khớp kiểu dữ liệu
        return playlistRepository.findAllByUserId(currentUser.getId())
                .stream()
                .map(playlistMapper::toPlaylistResponse)
                .toList();
    }

    public List<SongResponse> getSongsInPlaylist(Long playlistId) {
        User user = getCurrentUser();

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PLAYLIST));

        // Kiểm tra quyền sở hữu playlist
        if (!playlist.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return playlist.getPlaylistSongs().stream()
                .sorted(Comparator.comparingInt(PlaylistSong::getOrderInPlaylist))
                .map(PlaylistSong::getSong) // Lấy Song entity
                .map(songMapper::toSongResponse) // <<< SỬ DỤNG SONGMAPPER ĐỂ ÁNH XẠ
                .toList();
    }

    @Transactional
    public PlaylistResponse addSongToPlaylist(Long playlistId, Long songId, String userId) {
        Playlist playlist = playlistRepository.findByIdAndUserId(playlistId, userId)
                .orElseThrow(() -> new RuntimeException("Playlist not found or not owned by user"));

        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        boolean exists = playlist.getPlaylistSongs().stream()
                .anyMatch(ps -> ps.getSong().getId().equals(songId));
        if (exists) {
            throw new RuntimeException("Song already exists in playlist");
        }

        PlaylistSongId playlistSongId = new PlaylistSongId(playlistId, songId);

        PlaylistSong playlistSong = new PlaylistSong();
        playlistSong.setId(playlistSongId);
        playlistSong.setPlaylist(playlist);
        playlistSong.setSong(song);
        playlistSong.setOrderInPlaylist(playlist.getPlaylistSongs().size() + 1);

        playlist.addPlaylistSong(playlistSong);

        playlistRepository.save(playlist);

        return playlistMapper.toPlaylistResponse(playlist);
    }

    public PlaylistResponse updatePlaylist(Long playlistId, PlaylistRequest request) {
        User user = getCurrentUser();

        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PLAYLIST));

        if (!playlist.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        if (request.getName() != null && !request.getName().isEmpty()) {
            playlist.setName(request.getName());
        }

        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            playlist.setDescription(request.getDescription());
        }

        MultipartFile thumbnailFile = request.getThumbnailUrl();
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            try {
                String fileUrl = storageService.uploadFile(thumbnailFile);
                playlist.setThumbnailUrl(fileUrl);
            } catch (IOException e) {
                // Nên có logging ở đây
                throw new RuntimeException("Failed to upload thumbnail", e);
            }
        }

        Playlist updated = playlistRepository.save(playlist);
        return playlistMapper.toPlaylistResponse(updated);
    }
}