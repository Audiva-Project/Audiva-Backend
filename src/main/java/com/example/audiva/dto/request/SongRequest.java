package com.example.audiva.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SongRequest {
    String title;
    String genre;
    Integer duration;
    String audioUrl;

    private Long albumId;
    private List<Long> artistIds;
}
