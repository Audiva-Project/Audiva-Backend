package com.example.audiva.service;

import com.example.audiva.dto.request.ArtistRequest;
import com.example.audiva.entity.Artist;
import com.example.audiva.exception.AppException;
import com.example.audiva.exception.ErrorCode;
import com.example.audiva.mapper.ArtistMapper;
import com.example.audiva.repository.ArtistRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArtistService {

    ArtistRepository artistRepository;
    ArtistMapper artistMapper;
    StorageService storageService;

    public List<Artist> findAllArtists() {
        return artistRepository.findAll();
    }

    public Artist findArtistById(Long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ARTIST_NOT_FOUND));
    }

    public Artist createArtist(ArtistRequest request, MultipartFile file) throws IOException {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_ARTIST_NAME);
        }
        if (artistRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ARTIST_EXISTED);
        }

        if (file != null && !file.isEmpty()) {
            request.setAvatar(storageService.uploadFile(file));
        } else {
            request.setAvatar(null);
        }

        Artist artist = artistMapper.toArtist(request);
        return artistRepository.save(artist);
    }

    public Artist updateArtist(Long id, ArtistRequest request) {
        Artist artist = findArtistById(id);
        if (artistRepository.existsByName(request.getName()) &&
                !artist.getName().equalsIgnoreCase(request.getName())) {
            throw new AppException(ErrorCode.ARTIST_EXISTED);
        }
        artistMapper.updateArtistFromRequest(request, artist);
        return artistRepository.save(artist);
    }

    public void deleteArtist(Long id) {
        Artist artist = findArtistById(id);
        if (!artist.getSongs().isEmpty()) {
            throw new AppException(ErrorCode.ARTIST_HAS_SONGS);
        }
        artistRepository.delete(artist);
    }

    public List<Artist> searchArtistsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_SEARCH_TERM);
        }
        return artistRepository.findByNameContainingIgnoreCase(name);
    }
}
