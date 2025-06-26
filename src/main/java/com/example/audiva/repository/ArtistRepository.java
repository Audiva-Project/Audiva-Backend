package com.example.audiva.repository;

import com.example.audiva.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findByNameContainingIgnoreCase(String name);

    boolean existsByName(String name);

    Artist findArtistById(Long id);
}
