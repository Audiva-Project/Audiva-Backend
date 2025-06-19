package com.example.audiva.repository;

import com.example.audiva.entity.Album;
import com.example.audiva.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository <Song, Long> {
    List<Song> findByTitle(String title);
    List<Song> findByArtist_Name(String artistName);
    List<Song> findByAlbum_Title(String albumTitle);
    List<Song> findByGenre_Name(String genreName);
}
