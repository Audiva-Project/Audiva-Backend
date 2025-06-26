package com.example.audiva.service;

import com.example.audiva.dto.request.ArtistRequest;
import com.example.audiva.dto.response.ArtistResponse;
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

    public List<ArtistResponse> findAllArtists() {
        return artistRepository.findAll()
                .stream()
                .map(artistMapper::toArtistResponse)
                .toList();
    }

    public ArtistResponse findArtistById(Long id) {
        Artist curArtist = artistRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ARTIST_NOT_FOUND));
        return artistMapper.toArtistResponse(curArtist);
    }

    public ArtistResponse createArtist(ArtistRequest request, MultipartFile file) throws IOException {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_ARTIST_NAME);
        }
        if (artistRepository.existsByName(request.getName())) {
            throw new AppException(ErrorCode.ARTIST_EXISTED);
        }

        Artist artist = artistMapper.toArtist(request);
        if (file != null && !file.isEmpty()) {
            artist.setAvatar(storageService.uploadFile(file));
        } else {
            artist.setAvatar(null);
        }
        return artistMapper.toArtistResponse(artistRepository.save(artist));
    }

    public ArtistResponse updateArtist(Long id, ArtistRequest request, MultipartFile file) throws IOException {
        Artist artist = artistRepository.findArtistById(id);
        if (artistRepository.existsByName(request.getName()) &&
                !artist.getName().equalsIgnoreCase(request.getName())) {
            throw new AppException(ErrorCode.ARTIST_EXISTED);
        }
        artistMapper.updateArtistFromRequest(request, artist);

        if (file != null && !file.isEmpty()) {
            artist.setAvatar(storageService.uploadFile(file));
        } else {
            artist.setAvatar(null);
        }
        return artistMapper.toArtistResponse(artistRepository.save(artist));
    }

    public void deleteArtist(Long id) {
        Artist artist = artistRepository.findArtistById(id);
        if (!artist.getSongs().isEmpty()) {
            throw new AppException(ErrorCode.ARTIST_HAS_SONGS);
        }
        artistRepository.delete(artist);
    }

    public List<ArtistResponse> searchArtistsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new AppException(ErrorCode.INVALID_SEARCH_TERM);
        }
        return artistRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(artistMapper::toArtistResponse)
                .toList();
    }
}
