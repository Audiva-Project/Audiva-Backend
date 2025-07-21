package com.example.audiva.enums;

import lombok.Getter;

@Getter
public enum NotificationStatus {
    UNREAD("UNREAD"),
    READ("READ"),
    ARCHIVED("ARCHIVED");

    private final String status;

    NotificationStatus(String status) {
        this.status = status;
    }
}
