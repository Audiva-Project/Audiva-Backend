package com.example.audiva.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1002, "User not found", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1004, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1005, "User not existed", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(1006, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1007, "You do not have permission", HttpStatus.FORBIDDEN),
    INVALID_DOB(1008, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN(1009, "Invalid token", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN(1010, "Invalid refresh token", HttpStatus.UNAUTHORIZED),
    // Artist related errors
    ARTIST_EXISTED(1011, "Artist existed", HttpStatus.BAD_REQUEST),
    ARTIST_NOT_FOUND(1012, "Artist not found", HttpStatus.NOT_FOUND),
    ARTIST_HAS_SONGS(1013, "Artist has songs, cannot delete", HttpStatus.BAD_REQUEST),
    INVALID_SEARCH_TERM(1014, "Invalid search term", HttpStatus.BAD_REQUEST),
    INVALID_ARTIST_NAME(1015, "Artist name is required", HttpStatus.BAD_REQUEST),

    // SONG Error code
    SONG_EXISTED(1016, "Song existed", HttpStatus.BAD_REQUEST),
    SONG_NOT_FOUND(1017, "Song not found", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAILED(1018, "File upload failed", HttpStatus.INTERNAL_SERVER_ERROR),
    SONG_NOT_EXISTED(1025, "Song not existed", HttpStatus.NOT_FOUND),

    // ALBUM Error code
    ALBUM_EXISTED(1019, "Album existed", HttpStatus.BAD_REQUEST),
    SONG_NOT_IN_ALBUM(1019, "Song not in album", HttpStatus.BAD_REQUEST),
    ALBUM_NOT_FOUND(1020, "Album not found", HttpStatus.NOT_FOUND),

    // PREMIUM Error code
    PREMIUM_NOT_EXISTED(1021, "Premium not existed", HttpStatus.NOT_FOUND),
    PREMIUM_NOT_FOUND(1022, "Premium not found", HttpStatus.NOT_FOUND),
    INVALID_PREMIUM_PRICE(1023, "Invalid premium price", HttpStatus.BAD_REQUEST),
    PREMIUM_EXISTED(1024, "Premium existed", HttpStatus.BAD_REQUEST),
    PREMIUM_ALREADY_EXISTED(1025, "Premium already existed", HttpStatus.BAD_REQUEST),

    // PLAYLIST Error code
    PLAYLIST_EXISTED(1021, "Playlist existed", HttpStatus.BAD_REQUEST),
    INVALID_PLAYLIST(1022, "Invalid Playlist", HttpStatus.NOT_FOUND),

    // NOTIFICATIONS Error code
    NOTIFICATIONS_NOT_FOUND(1023, "Notifications not found", HttpStatus.NOT_FOUND),
    NOTIFICATION_NOT_FOUND(1024, "Notification not found", HttpStatus.NOT_FOUND),

    // UPLOAD Error code
    UPLOAD_FAILED(1025, "File upload failed", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_NOT_FOUND(1026, "File not found", HttpStatus.NOT_FOUND),
    INVALID_FILE_TYPE(1027, "Invalid file type", HttpStatus.BAD_REQUEST);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }
}
