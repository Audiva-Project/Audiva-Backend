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
        boolean exists;
        User user = null;

        if (userName != null) {
            user = userRepository.findByUsername(userName)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        }

        if (userName != null) {
            exists = listeningHistoryRepository
                    .findByUserIdAndSongId(user.getId(), songId)
                    .isPresent();
        } else {
            exists = listeningHistoryRepository
                    .findByAnonymousIdAndSongId(anonymousId, songId)
                    .isPresent();
        }

        if (exists) {
            return;
        }

        ListeningHistory history = new ListeningHistory();
        history.setSong(songRepository.findById(songId)
                .orElseThrow(() -> new AppException(ErrorCode.SONG_NOT_EXISTED)));
        history.setListenedAt(LocalDateTime.now());

        if (user != null) {
            history.setUser(user);
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

            // 2. Kiểm tra user đã có record nghe bài này chưa
            Optional<ListeningHistory> existingUserHistory = listeningHistoryRepository.findByUserIdAndSongId(user.getId(), songId);

            if (existingUserHistory.isPresent()) {
                ListeningHistory listeningHistory = existingUserHistory.get();

                // 3. Nếu anonymous nghe gần đây hơn → update listenedAt
                if (anonymousHistory.getListenedAt().isAfter(listeningHistory.getListenedAt())) {
                    listeningHistory.setListenedAt(anonymousHistory.getListenedAt());
                    listeningHistoryRepository.save(listeningHistory);
                }
                
            } else {
                // 5. Nếu user chưa nghe bài này → gán vào user
                anonymousHistory.setUser(user);
//                anonymousHistory.setAnonymousId(null);
                listeningHistoryRepository.save(anonymousHistory);
            }
        }
    }
}
