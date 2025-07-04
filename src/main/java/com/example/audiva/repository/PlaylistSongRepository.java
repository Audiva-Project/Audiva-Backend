package com.example.audiva.repository;

import com.example.audiva.entity.PlaylistSong;
import com.example.audiva.entity.PlaylistSongId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaylistSongRepository extends JpaRepository<PlaylistSong, PlaylistSongId> {
}
