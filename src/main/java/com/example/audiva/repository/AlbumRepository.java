package com.example.audiva.repository;

import com.example.audiva.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByArtist_Name(String name);
    List<Album> findByTitle(String title);
}

