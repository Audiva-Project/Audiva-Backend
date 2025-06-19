package com.example.audiva.service;

import com.example.audiva.dto.request.ArtistRequest;
import com.example.audiva.entity.Artist;
import com.example.audiva.mapper.ArtistMapper;
import com.example.audiva.repository.ArtistRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArtistService {

    ArtistRepository artistRepository;
    ArtistMapper artistMapper;

    public List<Artist> findAllArtists() {
        return artistRepository.findAll();
    }

    public Artist findArtistById(Long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artist not found with id: " + id));
    }

    public Artist createArtist(ArtistRequest request) {
        Artist artist = artistMapper.toArtist(request);
        return artistRepository.save(artist);
    }

    public Artist updateArtist(Long id, ArtistRequest request) {
        Artist artist = findArtistById(id);
        artistMapper.updateArtistFromRequest(request, artist);
        return artistRepository.save(artist);
    }

    public void deleteArtist(Long id) {
        Artist artist = findArtistById(id);
        artistRepository.delete(artist);
    }

    public List<Artist> searchArtistsByName(String name) {
        return artistRepository.findByNameContainingIgnoreCase(name);
    }
}
