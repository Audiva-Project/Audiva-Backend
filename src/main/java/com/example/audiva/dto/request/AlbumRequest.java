package com.example.audiva.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AlbumRequest {
    private String title;
    private String thumbnailUrl;
    private Long artistId;
    private LocalDate releaseDate;
}
