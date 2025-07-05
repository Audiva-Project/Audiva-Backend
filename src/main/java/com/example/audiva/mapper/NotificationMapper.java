package com.example.audiva.mapper;

import com.example.audiva.dto.request.NotificationRequest;
import com.example.audiva.dto.response.NotificationResponse;
import com.example.audiva.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    NotificationResponse toResponse(Notification notification);

    Notification toEntity(NotificationRequest request);
}
