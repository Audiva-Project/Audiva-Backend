package com.example.audiva.repository;

import com.example.audiva.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> getSongById(Long id);

    Page<Song> findAll(Pageable pageable);

    Page<Song> findByCreatedBy(String createdBy, Pageable pageable);
}
