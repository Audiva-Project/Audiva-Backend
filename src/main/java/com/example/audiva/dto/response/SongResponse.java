package com.example.audiva.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class SongResponse {
    private Long id;
    private String title;
    private String genre;
    private Integer duration;
    private String audioUrl;

    private AlbumSummary album;
    private List<ArtistSummary> artists;

    @Data
    public static class AlbumSummary {
        private Long id;
        private String title;
    }

    @Data
    public static class ArtistSummary {
        private Long id;
        private String name;
    }
}
