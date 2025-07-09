package com.example.audiva.service;

import com.example.audiva.dto.response.ListeningHistoryResponse;
import com.example.audiva.entity.ListeningHistory;
import com.example.audiva.entity.User;
import com.example.audiva.exception.AppException;
import com.example.audiva.exception.ErrorCode;
import com.example.audiva.mapper.ListeningHistoryMapper;
import com.example.audiva.repository.ListeningHistoryRepository;
import com.example.audiva.repository.SongRepository;
import com.example.audiva.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ListeningHistoryService {
    @Autowired
    ListeningHistoryRepository listeningHistoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SongRepository songRepository;

    @Autowired
    ListeningHistoryMapper listeningHistoryMapper;

    public void save(String userName, Long songId, String anonymousId) {
        User user = null;

        if (userName != null) {
            user = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        }

        if (userName != null) {
            Optional<ListeningHistory> listeningHistoryOptional = listeningHistoryRepository
                    .findByUserIdAndSongId(user.getId(), songId);
            // update listenedAt if exists
            if (listeningHistoryOptional.isPresent()) {
                ListeningHistory listeningHistory = listeningHistoryOptional.get();
                listeningHistory.setListenedAt(LocalDateTime.now());
                listeningHistoryRepository.save(listeningHistory);
                return;
            }
        } else {
            Optional<ListeningHistory> listeningHistoryOptional = listeningHistoryRepository
                    .findByAnonymousIdAndSongId(anonymousId, songId);
            // update listenedAt if exists
            if (listeningHistoryOptional.isPresent()) {
                ListeningHistory listeningHistory = listeningHistoryOptional.get();
                listeningHistory.setListenedAt(LocalDateTime.now());
                listeningHistoryRepository.save(listeningHistory);
                return;
            }
        }

        ListeningHistory history = new ListeningHistory();
        history.setSong(songRepository.findById(songId)
                .orElseThrow(() -> new AppException(ErrorCode.SONG_NOT_EXISTED)));
        history.setListenedAt(LocalDateTime.now());

        if (user != null) {
            history.setUser(user);
            history.setAnonymousId(null);
        } else {
            history.setUser(null);
            history.setAnonymousId(anonymousId);
        }

        listeningHistoryRepository.save(history);
    }

    public List<ListeningHistoryResponse> getHistoryForUser(String userName) {
        User user = userRepository.findByUsername(userName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return listeningHistoryRepository.findByUserId(user.getId()).stream()
                .map(listeningHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ListeningHistoryResponse> getHistoryForAnonymous(String anonymousId) {
        return listeningHistoryRepository.findByAnonymousId(anonymousId).stream()
                .map(listeningHistoryMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void mergeAnonymousHistory(String anonymousId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<ListeningHistory> anonymousHistories = listeningHistoryRepository.findByAnonymousId(anonymousId);

        for (ListeningHistory anonymousHistory : anonymousHistories) {
            Long songId = anonymousHistory.getSong().getId();

            // Kiểm tra user đã có record nghe bài này chưa
            Optional<ListeningHistory> existingUserHistory = listeningHistoryRepository.findByUserIdAndSongId(user.getId(), songId);

            if (existingUserHistory.isPresent()) {
                ListeningHistory listeningHistory = existingUserHistory.get();

                // Nếu anonymous nghe gần đây hơn → update listenedAt
                if (anonymousHistory.getListenedAt().isAfter(listeningHistory.getListenedAt())) {
                    listeningHistory.setListenedAt(anonymousHistory.getListenedAt());
                    listeningHistoryRepository.save(listeningHistory);
                }

            } else {
                // Nếu user chưa nghe bài này → gán vào user
                anonymousHistory.setUser(user);
//                anonymousHistory.setAnonymousId(null);
                listeningHistoryRepository.save(anonymousHistory);
            }
        }
    }
}
