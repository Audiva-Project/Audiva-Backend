package com.example.audiva.service;

import com.example.audiva.entity.Album;
import com.example.audiva.entity.Artist;
import com.example.audiva.entity.Genre;
import com.example.audiva.entity.Song;
import com.example.audiva.repository.AlbumRepository;
import com.example.audiva.repository.GenreRepository;
import com.example.audiva.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicCatalogService {

    @Autowired
    private SongRepository songRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private GenreRepository genreRepository;

    // Song management
    public Song addSong(Song song) {
        return songRepository.save(song);
    }

    public List<Song> getSongsByArtist(String artist) {
        return songRepository.findByArtist_Name(artist);
    }

    public List<Song> getSongsByGenre(String genre) {
        return songRepository.findByGenre_Name(genre);
    }

    // Album management
    public Album addAlbum(Album album) {
        return albumRepository.save(album);
    }

    public List<Album> getAlbumsByArtist(String artist) {
        return albumRepository.findByArtist_Name(artist);
    }

    // Genre management
    public Genre addGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    public Genre getGenreByName(String name) {
        return genreRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Genre not found"));
    }
}

