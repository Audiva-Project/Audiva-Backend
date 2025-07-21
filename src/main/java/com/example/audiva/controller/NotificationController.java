package com.example.audiva.controller;

import com.example.audiva.dto.request.ApiResponse;
import com.example.audiva.dto.request.NotificationRequest;
import com.example.audiva.dto.response.NotificationResponse;
import com.example.audiva.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/me")
    public ApiResponse<List<NotificationResponse>> getNotifications() {
        List<NotificationResponse> responses = notificationService.getNotifications();
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(responses)
                .message("Notifications retrieved successfully")
                .build();
    }

    @PostMapping
    public ApiResponse<NotificationResponse> createNotification(
            @RequestBody NotificationRequest request) {
        NotificationResponse response = notificationService.createNotification(request);
        return ApiResponse.<NotificationResponse>builder()
                .result(response)
                .message("Notification created successfully")
                .build();
    }

    @PostMapping("/{id}/read")
    public ApiResponse<Void> markAsRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ApiResponse.<Void>builder()
                .result(null)
                .message("Notification marked as read successfully")
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ApiResponse.<Void>builder()
                .result(null)
                .message("Notification deleted successfully")
                .build();
    }
}
