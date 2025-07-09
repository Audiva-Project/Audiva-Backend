package com.example.audiva.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class AlbumResponse {
    private Long id;
    private String title;
    private String thumbnailUrl;
    private LocalDate releaseDate;
    private List<SongResponse> songs;
    private ArtistSummary artist;

    @Data
    @Builder
    public static class ArtistSummary {
        private Long id;
        private String name;
    }
}
