package com.example.audiva.dto.response;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AlbumResponse {
    private Long id;
    private String title;
    private String thumbnailUrl;
    private LocalDate releaseDate;
    private Long artistId;
    private String artistName;
}
