package com.example.audiva.service;

import com.example.audiva.dto.request.AlbumRequest;
import com.example.audiva.dto.response.AlbumResponse;
import com.example.audiva.entity.Album;
import com.example.audiva.entity.Song;
import com.example.audiva.exception.AppException;
import com.example.audiva.exception.ErrorCode;
import com.example.audiva.mapper.AlbumMapper;
import com.example.audiva.repository.AlbumRepository;
import com.example.audiva.repository.SongRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AlbumService {

    AlbumRepository albumRepository;
    UploadService uploadService;
    AlbumMapper albumMapper;
    SongRepository songRepository;

    public List<AlbumResponse> getAllAlbums() {
        return albumRepository.findAll()
                .stream()
                .map(albumMapper::toAlbumResponse)
                .toList();
    }

    public AlbumResponse getAlbumById(Long id) {
        Album album = albumRepository.findWithSongsById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND));
        return albumMapper.toAlbumResponse(album);
    }

    public AlbumResponse createAlbum(AlbumRequest request) throws IOException {
        Album album = albumMapper.toAlbum(request);

        if (request.getThumbnailFile() != null && !request.getThumbnailFile().isEmpty()) {
            album.setThumbnailUrl(uploadService.uploadFile(request.getThumbnailFile()));
        }

        Album savedAlbum = albumRepository.save(album);

        if (request.getSongIds() != null && !request.getSongIds().isEmpty()) {
            List<Song> songs = songRepository.findAllById(request.getSongIds());

            if (songs.size() != request.getSongIds().size()) {
                throw new AppException(ErrorCode.SONG_NOT_FOUND);
            }

            for (Song song : songs) {
                song.setAlbum(savedAlbum);
            }

            savedAlbum.setSongs(songs);
            songRepository.saveAll(songs);
        }

        savedAlbum = albumRepository.findWithSongsById(savedAlbum.getId())
                .orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND));

        return albumMapper.toAlbumResponse(savedAlbum);
    }

    public AlbumResponse updateAlbum(Long id, AlbumRequest request) {
        Album existAlbum = albumRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND));

        if (request.getTitle() != null) {
            existAlbum.setTitle(request.getTitle());
        }

        if (request.getSongIds() != null) {
            existAlbum.setSongs(songRepository.findAllById(request.getSongIds()));
        }

        if (request.getReleaseDate() != null) {
            existAlbum.setReleaseDate(request.getReleaseDate());
        }

        if (request.getThumbnailFile() != null && !request.getThumbnailFile().isEmpty()) {
            try {
                existAlbum.setThumbnailUrl(uploadService.uploadFile(request.getThumbnailFile()));
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        return albumMapper.toAlbumResponse(albumRepository.save(existAlbum));
    }

    public void deleteAlbum(Long id) {
        if (!albumRepository.existsById(id)) {
            throw new AppException(ErrorCode.ALBUM_NOT_FOUND);
        }
        albumRepository.deleteById(id);
    }
}
