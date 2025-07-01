package com.example.audiva.service;

import com.example.audiva.entity.ListeningHistory;
import com.example.audiva.repository.ListeningHistoryRepository;
import com.example.audiva.repository.SongRepository;
import com.example.audiva.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ListeningHistoryService {
    @Autowired
    ListeningHistoryRepository listeningHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SongRepository songRepository;

    public void save(String userId, Long songId, String anonymousId) {
        ListeningHistory history = new ListeningHistory();
        if(userId != null) {
            history.setUser(userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId)));
        } else {
                history.setAnonymousId(anonymousId);
        }
        history.setSong(songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found")));

        history.setListenedAt(LocalDateTime.now());
        listeningHistoryRepository.save(history);
    }
}
