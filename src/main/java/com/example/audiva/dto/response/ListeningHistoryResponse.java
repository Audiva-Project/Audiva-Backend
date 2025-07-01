package com.example.audiva.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class ListeningHistoryResponse {
    private SongResponse song;
    private LocalDateTime listenedAt;
}
