package com.example.audiva.dto.response;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class AlbumResponse {
    private Long id;
    private String title;
    private String thumbnailUrl;
    private LocalDate releaseDate;
    private Long artistId;
    private String artistName;
    private List<SongResponse> songs;
}
