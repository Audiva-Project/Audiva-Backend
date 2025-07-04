package com.example.audiva.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SongResponse {
    private Long id;
    private String title;
    private String genre;
    private String audioUrl;
    private String thumbnailUrl;
    private boolean isPremium;
    private String createdBy;
    private Long playCount;

    private AlbumSummary album;
    private List<ArtistSummary> artists;

    @Data
    public static class AlbumSummary {
        private Long id;
        private String title;
    }

    @Data
    @Builder
    public static class ArtistSummary {
        private Long id;
        private String name;
    }
}
