package com.example.audiva.dto.request;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationRequest {
    private Long userId;
    private String title;
    private String message;
    private Boolean isRead;
    private LocalDateTime readAt;
}
