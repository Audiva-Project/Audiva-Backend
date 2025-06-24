package com.example.audiva.service;

import com.example.audiva.dto.request.AlbumRequest;
import com.example.audiva.dto.response.AlbumResponse;
import com.example.audiva.entity.Album;
import com.example.audiva.entity.Artist;
import com.example.audiva.mapper.AlbumMapper;
import com.example.audiva.repository.AlbumRepository;
import com.example.audiva.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlbumService {
    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumMapper albumMapper;

    public List<AlbumResponse>  getAllAlbum() {
        return albumRepository.findAll().stream()
                .map(albumMapper::toAlbumResponse)
                .toList();
    }

    public AlbumResponse getAlbumById (Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Album not found"));
        return albumMapper.toAlbumResponse(album);
    }

    public AlbumResponse createAlbum (AlbumRequest request) {
        Album album = albumMapper.toAlbum(request);
        Artist artist = artistRepository.findById(request.getArtistId())
                .orElseThrow(()-> new RuntimeException("Artist not found"));
        album.setArtist(artist);
        return albumMapper.toAlbumResponse(albumRepository.save(album));

    };

    public AlbumResponse updateAlbum(Long id, AlbumRequest request) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Album not found with id "+id));
        albumMapper.updateAlbumFromRequest(request, album);
        Artist artist = artistRepository.findById(request.getArtistId())
                .orElseThrow(() -> new RuntimeException("Artist not found"));
        album.setArtist(artist);
        return albumMapper.toAlbumResponse(albumRepository.save(album));
    }

    public void deleteAlbum(Long id) {
        albumRepository.deleteById(id);
    }
}
