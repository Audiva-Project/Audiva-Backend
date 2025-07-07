package com.example.audiva.repository;

import com.example.audiva.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);

    Artist findArtistById(Long id);

    @Query("SELECT a FROM Artist a JOIN a.songs s WHERE s.title LIKE %:songTitle%")
    Artist findArtistBySongTitle(@Param("songTitle") String songTitle);

    @Query("SELECT DISTINCT a FROM Artist a JOIN a.albums al WHERE al.title LIKE %:albumTitle%")
    List<Artist> findArtistByAlbumTitle(@Param("albumTitle") String albumTitle);

    List<Artist> findDistinctBySongs_TitleContainingIgnoreCase(String songTitle);
}
