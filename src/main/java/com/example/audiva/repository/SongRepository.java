package com.example.audiva.repository;

import com.example.audiva.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
//    List<Song> findByArtist(String artist);
//    List<Song> findByAlbum (String album);
//    List<Song> findByGenre (String genre);
    Boolean existByTitle(String title);
}
