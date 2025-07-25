package com.example.audiva.repository;

import com.example.audiva.entity.ListeningHistory;
import com.example.audiva.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, Long> {
    // sort by listenedAt desc
    @Query("SELECT lh FROM ListeningHistory lh WHERE lh.user.id = :userId ORDER BY lh.listenedAt DESC")
    List<ListeningHistory> findByUserId(String userId);

    @Query("SELECT lh FROM ListeningHistory lh WHERE lh.anonymousId = :anonymousId ORDER BY lh.listenedAt DESC")
    List<ListeningHistory> findByAnonymousId(@Param("anonymousId") String anonymousId);

    @Query("SELECT lh FROM ListeningHistory lh WHERE lh.user.id = :userId AND lh.song.id = :songId")
    Optional<ListeningHistory> findByUserIdAndSongId(String userId, Long songId);

    @Query("SELECT lh FROM ListeningHistory lh WHERE lh.anonymousId = :anonymousId AND lh.song.id = :songId")
    Optional<ListeningHistory> findByAnonymousIdAndSongId(String anonymousId, Long songId);

    void deleteAllBySong(Song song);
}
