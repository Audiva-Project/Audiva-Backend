package com.example.audiva.repository;

import com.example.audiva.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    // Additional query methods can be defined here if needed
    @Query("SELECT a FROM Album a LEFT JOIN FETCH a.songs WHERE a.id = :id")
    Optional<Album> findWithSongsById(@Param("id") Long id);

}

