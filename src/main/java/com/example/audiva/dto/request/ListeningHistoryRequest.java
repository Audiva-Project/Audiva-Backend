package com.example.audiva.dto.request;

import lombok.Data;

@Data
public class ListeningHistoryRequest {
        private Long songId;
        private String anonymousId;
}
