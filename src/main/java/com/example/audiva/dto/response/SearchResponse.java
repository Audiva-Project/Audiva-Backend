package com.example.audiva.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    private List<SongResponse> songs;
    private List<ArtistResponse> artists;
    private List<AlbumResponse> albums;
}

