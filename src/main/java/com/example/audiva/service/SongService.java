package com.example.audiva.service;

import com.example.audiva.dto.request.SongRequest;
import com.example.audiva.dto.response.SongResponse;
import com.example.audiva.entity.Album;
import com.example.audiva.entity.Artist;
import com.example.audiva.entity.Song;
import com.example.audiva.enums.Genre;
import com.example.audiva.exception.AppException;
import com.example.audiva.exception.ErrorCode;
import com.example.audiva.mapper.SongMapper;
import com.example.audiva.repository.ArtistRepository;
import com.example.audiva.repository.SongRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SongService {

    SongRepository songRepository;
    SongMapper songMapper;
    StorageService storageService;
    ArtistRepository artistRepository;


    public List<SongResponse> getAllSong() {
        return songRepository.findAll()
                .stream()
                .map(songMapper::toSongResponse)
                .toList();
    }

    public SongResponse getSongById(Long id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SONG_NOT_FOUND));
        return songMapper.toSongResponse(song);
    }

    public SongResponse createSong(SongRequest request) throws IOException {

        Song song = songMapper.toSong(request);
        if (song.getGenre() == null) {
            song.setGenre(Genre.OTHER);
        }
        song.setAudioUrl(storageService.uploadFile(request.getAudioFile()));
        song.setThumbnailUrl(storageService.uploadFile(request.getThumbnailFile()));

        // Gán Album nếu có
//        if (request.getAlbumId() != null) {
//            Album album = albumRepository.findById(request.getAlbumId())
//                    .orElseThrow(() -> new EntityNotFoundException("Album not found: " + request.getAlbumId()));
//            song.setAlbum(album);
//        }

        if (request.getArtistIds() != null && !request.getArtistIds().isEmpty()) {
            List<Artist> artists = artistRepository.findAllById(request.getArtistIds());
            if (artists.size() != request.getArtistIds().size()) {
                throw new AppException(ErrorCode.ARTIST_NOT_FOUND);
            }
            song.setArtists(artists);
        }

        return songMapper.toSongResponse(songRepository.save(song));
    }

    public SongResponse updateSong(Long id, SongRequest request) {
        Song existSong = songRepository.getSongById(id);
        songMapper.updateSongFromRequest(request, existSong);
        return songMapper.toSongResponse(songRepository.save(existSong));
    }

    public void deleteSong(Long id) {
        songRepository.deleteById(id);
    }
}
