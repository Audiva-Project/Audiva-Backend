package com.example.audiva.repository;

import com.example.audiva.entity.Song;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> getSongById(Long id);

    // sort by
    @Query("SELECT s FROM Song s ORDER BY s.playCount DESC")
    Page<Song> findAll(Pageable pageable);

    Page<Song> findByCreatedBy(String createdBy, Pageable pageable);

    List<Song> findByTitleContainingIgnoreCase(String keyword);

    List<Song> findByArtists_NameContainingIgnoreCase(String artistName);

    List<Song> findByAlbum_TitleContainingIgnoreCase(String albumTitle);

    // DESC BY playCount
    @Query("SELECT s FROM Song s ORDER BY s.playCount DESC")
    Page<Song> findByArtists_Id(Long artistId, Pageable pageable);
}
