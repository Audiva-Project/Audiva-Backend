package com.example.audiva.service;

import com.example.audiva.dto.request.SongRequest;
import com.example.audiva.dto.response.SongResponse;
import com.example.audiva.entity.Artist;
import com.example.audiva.entity.Lyrics;
import com.example.audiva.entity.Song;
import com.example.audiva.enums.Genre;
import com.example.audiva.exception.AppException;
import com.example.audiva.exception.ErrorCode;
import com.example.audiva.mapper.SongMapper;
import com.example.audiva.repository.AlbumRepository;
import com.example.audiva.repository.ArtistRepository;
import com.example.audiva.repository.LyricsRepository;
import com.example.audiva.repository.SongRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SongService {

    SongRepository songRepository;
    SongMapper songMapper;
    StorageService storageService;
    ArtistRepository artistRepository;
    PremiumService premiumService;
    AlbumRepository albumRepository;
    LyricsRepository lyricsRepository;

    public Page<SongResponse> getAllSong(Pageable pageable) {
        Page<Song> songs = songRepository.findAll(pageable);
        return songs.map(songMapper::toSongResponse);
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

        if (request.getIsPremium() != null) {
            song.setPremium(Boolean.parseBoolean(request.getIsPremium()));
        }

        if (request.getArtistIds() != null && !request.getArtistIds().isEmpty()) {
            List<Artist> artists = artistRepository.findAllById(request.getArtistIds());
            if (artists.size() != request.getArtistIds().size()) {
                throw new AppException(ErrorCode.ARTIST_NOT_FOUND);
            }
            song.setArtists(artists);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            song.setCreatedBy(username);
            song.setCreatedDate(java.time.LocalDateTime.now());
        } else {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return songMapper.toSongResponse(songRepository.save(song));
    }

    public SongResponse updateSong(Long id, SongRequest request) {
        Song existSong = songRepository.getSongById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SONG_NOT_FOUND));
        songMapper.updateSongFromRequest(request, existSong);

        if (existSong.getGenre() == null) {
            existSong.setGenre(Genre.OTHER);
        }

        // check premium user


        if (request.getAudioFile() != null) {
            try {
                existSong.setAudioUrl(storageService.uploadFile(request.getAudioFile()));
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        if (request.getThumbnailFile() != null) {
            try {
                existSong.setThumbnailUrl(storageService.uploadFile(request.getThumbnailFile()));
            } catch (IOException e) {
                throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }

        if (request.getArtistIds() != null && !request.getArtistIds().isEmpty()) {
            List<Artist> artists = artistRepository.findAllById(request.getArtistIds());
            if (artists.size() != request.getArtistIds().size()) {
                throw new AppException(ErrorCode.ARTIST_NOT_FOUND);
            }
            existSong.setArtists(artists);
        }

        if (request.getIsPremium() != null) {
            boolean isPremium = Boolean.parseBoolean(request.getIsPremium());
            existSong.setPremium(isPremium);
        }
        // change album association if albumId is provided
        if (request.getAlbumId() != null) {
            existSong.setAlbum(albumRepository.getAlbumById(request.getAlbumId())
                    .orElseThrow(() -> new AppException(ErrorCode.ALBUM_NOT_FOUND)));
        } else {
            existSong.setAlbum(null);
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            existSong.setModifiedBy(username);
            existSong.setModifiedDate(java.time.LocalDateTime.now());
        } else {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        return songMapper.toSongResponse(songRepository.save(existSong));

    }

    public void deleteSong(Long id) {
        songRepository.deleteById(id);
    }

    public Song getSongEntityById(Long id) {
        return songRepository.getSongById(id)
                .orElseThrow(() -> new AppException(ErrorCode.SONG_NOT_FOUND));
    }

    // get song by createdBy
    public Page<SongResponse> getSongsCreatedByMe(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        String username = authentication.getName();

        Page<Song> songs = songRepository.findByCreatedBy(username, pageable);
        return songs.map(songMapper::toSongResponse);
    }

    public void increasePlayCount(Long songId) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new AppException(ErrorCode.SONG_NOT_FOUND));

        if (song.getPlayCount() == null) {
            song.setPlayCount(1L);
        } else {
            song.setPlayCount(song.getPlayCount() + 1);
        }

        songRepository.save(song);
    }

    public Optional<Lyrics> getLyricsBySongId(Long id) {
        return songRepository.findById(id)
                .map(Song::getLyrics);
    }

    @Transactional
    public Lyrics updateLyrics(Long songId, String newLyricsContent) {
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new AppException(ErrorCode.SONG_NOT_FOUND));

        Lyrics lyrics = song.getLyrics();
        if (lyrics == null) {
            // Nếu chưa có, tạo mới
            lyrics = new Lyrics();
            lyrics.setSong(song);
        }

        lyrics.setContent(newLyricsContent);
        lyricsRepository.save(lyrics);

        return lyrics;
    }

    public Page<SongResponse> getSongsByArtistId(Long artistId, Pageable pageable) {
        return songRepository.findByArtists_IdOrderByPlayCountDesc(artistId, pageable)
                .map(songMapper::toSongResponse);
    }
}
