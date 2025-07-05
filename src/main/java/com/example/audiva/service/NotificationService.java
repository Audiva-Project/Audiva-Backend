package com.example.audiva.service;

import com.example.audiva.dto.request.NotificationRequest;
import com.example.audiva.dto.response.NotificationResponse;
import com.example.audiva.entity.Notification;
import com.example.audiva.entity.User;
import com.example.audiva.exception.AppException;
import com.example.audiva.exception.ErrorCode;
import com.example.audiva.mapper.NotificationMapper;
import com.example.audiva.repository.NotificationRepository;
import com.example.audiva.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class NotificationService {
    NotificationRepository notificationRepository;
    NotificationMapper notificationMapper;
    UserRepository userRepository;

    public List<NotificationResponse> getNotifications() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        return notificationRepository.findByUserId(user.getId())
                .map(notifications -> notifications.stream()
                        .map(notificationMapper::toResponse)
                        .toList())
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATIONS_NOT_FOUND));
    }

    public NotificationResponse createNotification(NotificationRequest request) {
        var notification = notificationMapper.toEntity(request);
        notification.setCreatedDate(LocalDateTime.now());
        notification = notificationRepository.save(notification);
        return notificationMapper.toResponse(notification);
    }

    public void createNotification(User user, String title, String message) {
        Notification notification = Notification.builder()
                .user(user)
                .title(title)
                .message(message)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }


    public void markAsRead(Long notificationId) {
        var notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_FOUND));

        notification.setIsRead(true);
        notification.setReadAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    public void deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new AppException(ErrorCode.NOTIFICATION_NOT_FOUND);
        }
        notificationRepository.deleteById(notificationId);
    }
}
