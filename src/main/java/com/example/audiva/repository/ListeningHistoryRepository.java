package com.example.audiva.repository;

import com.example.audiva.entity.ListeningHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ListeningHistoryRepository extends JpaRepository<ListeningHistory, Long> {
    List<ListeningHistory> findByUserId(String userId);

    @Query("SELECT lh FROM ListeningHistory lh WHERE lh.anonymousId = :anonymousId")
    List<ListeningHistory> findByAnonymousId(@Param("anonymousId") String anonymousId);
}
