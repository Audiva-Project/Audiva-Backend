package com.example.audiva.service;

import com.example.audiva.dto.request.PlaylistRequest;
import com.example.audiva.dto.response.PlaylistResponse;
import com.example.audiva.dto.response.SongResponse;
import com.example.audiva.entity.*;
import com.example.audiva.exception.AppException;
import com.example.audiva.exception.ErrorCode;
import com.example.audiva.mapper.PlaylistMapper;
import com.example.audiva.mapper.SongMapper;
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
    UploadService uploadService;
    SongMapper songMapper;

    public PlaylistResponse createPlaylist(PlaylistRequest request) {
        User user = getCurrentUser();

        MultipartFile thumbnailFile = request.getThumbnailFile();

        String fileUrl;
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            try {
                fileUrl = uploadService.uploadFile(thumbnailFile);
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_NOT_FOUND);
            }
        } else {
            fileUrl = "https://res.cloudinary.com/ddoesja29/image/upload/v1753263070/audiva/img/1751428697154playlist.jpg";
        }

        Playlist playlist = Playlist.builder()
                .name(request.getName())
                .description(request.getDescription())
                .thumbnailUrl(fileUrl)
                .user(user)
                .build();

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
        return playlistRepository.findAllByUserId(currentUser.getId())
                .stream()
                .map(playlistMapper::toPlaylistResponse)
                .toList();
    }

    public List<SongResponse> getSongsInPlaylist(Long playlistId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PLAYLIST));

        return playlist.getPlaylistSongs().stream()
                .sorted(Comparator.comparingInt(PlaylistSong::getOrderInPlaylist))
                .map(PlaylistSong::getSong) // Lấy Song entity
                .map(songMapper::toSongResponse)
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
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PLAYLIST));

//        if (!playlist.getCreatedBy().equals(user.getId())) {
//            throw new AppException(ErrorCode.UNAUTHORIZED);
//        }

        if (request.getName() != null && !request.getName().isEmpty()) {
            playlist.setName(request.getName());
        }

        if (request.getDescription() != null && !request.getDescription().isEmpty()) {
            playlist.setDescription(request.getDescription());
        }

        MultipartFile thumbnailFile = request.getThumbnailFile();
        if (thumbnailFile != null && !thumbnailFile.isEmpty()) {
            try {
                String fileUrl = uploadService.uploadFile(thumbnailFile);
                playlist.setThumbnailUrl(fileUrl);
            } catch (IOException e) {
                // Nên có logging ở đây
                throw new RuntimeException("Failed to upload thumbnail", e);
            }
        }

        Playlist updated = playlistRepository.save(playlist);
        return playlistMapper.toPlaylistResponse(updated);
    }

    public PlaylistResponse deleteSongFromPlaylist(Long playlistId, Long songId, String userId) {
        Playlist playlist = playlistRepository.findByIdAndUserId(playlistId, userId)
                .orElseThrow(() -> new RuntimeException("Playlist not found or not owned by user"));

        PlaylistSongId playlistSongId = new PlaylistSongId(playlistId, songId);

        PlaylistSong playlistSong = playlistSongRepository.findById(playlistSongId)
                .orElseThrow(() -> new RuntimeException("Song not found in playlist"));

        playlistSongRepository.delete(playlistSong);

        playlist.getPlaylistSongs().removeIf(ps -> ps.getSong().getId().equals(songId));

        int order = 1;
        for (PlaylistSong ps : playlist.getPlaylistSongs()) {
            ps.setOrderInPlaylist(order++);
        }

        playlistRepository.save(playlist);

        return playlistMapper.toPlaylistResponse(playlist);
    }

    @Transactional
    public void deletePlaylist(Long playlistId, String userId) {
        Playlist playlist = playlistRepository.findById(playlistId)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_PLAYLIST));

        if (!playlist.getUser().getId().equals(userId)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        playlistRepository.delete(playlist);
    }

}