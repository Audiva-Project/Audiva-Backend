package com.example.audiva.repository;

import com.example.audiva.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    // Additional query methods can be defined here if needed
    @Query("SELECT a FROM Album a LEFT JOIN FETCH a.songs WHERE a.id = :id")
    Optional<Album> findWithSongsById(@Param("id") Long id);

    Optional<Album> getAlbumById(Long id); // This method is redundant if you use findWithSongsById, but kept for compatibility

    List<Album> findByTitleContainingIgnoreCase(String title);

    List<Album> findByArtist_NameContainingIgnoreCase(String artistName);

    List<Album> findDistinctBySongs_TitleContainingIgnoreCase(String keyword);

    @Query("""
              SELECT a FROM Album a 
              JOIN a.artist ar 
              WHERE LOWER(ar.name) LIKE LOWER(CONCAT('%', :artistName, '%'))
            """)
    List<Album> searchAlbumsByArtistName(@Param("artistName") String artistName);
}

