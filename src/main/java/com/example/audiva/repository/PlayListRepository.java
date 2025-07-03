package com.example.audiva.repository;

import com.example.audiva.entity.Playlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlayListRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findAllByUserId(String userId);
    Optional<Playlist> findByIdAndUserId(Long playlistId, String userId);
    boolean existsByUserIdAndName(String userId, String name);
}
