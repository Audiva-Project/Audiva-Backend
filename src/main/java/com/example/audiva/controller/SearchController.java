package com.example.audiva.controller;

import com.example.audiva.dto.response.AlbumResponse;
import com.example.audiva.dto.response.ArtistResponse;
import com.example.audiva.dto.response.SearchResponse;
import com.example.audiva.dto.response.SongResponse;
import com.example.audiva.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    // ----------- Tìm kiếm Bài hát -----------
    @GetMapping("/songs")
    public List<SongResponse> searchSongsByName(@RequestParam String keyword) {
        return searchService.searchSongsByName(keyword);
    }

    @GetMapping("/songs/by-artist")
    public List<SongResponse> searchSongsByArtist(@RequestParam String artistName) {
        return searchService.searchSongsByArtist(artistName);
    }

    @GetMapping("/songs/by-album")
    public List<SongResponse> searchSongsByAlbum(@RequestParam String albumName) {
        return searchService.searchSongsByAlbum(albumName);
    }

    // ----------- Tìm kiếm Nghệ sĩ -----------
    @GetMapping("/artists")
    public List<ArtistResponse> searchArtistsByName(@RequestParam String name) {
        return searchService.searchArtistsByName(name);
    }

    @GetMapping("/artists/by-song")
    public ArtistResponse searchArtistBySong(@RequestParam String songTitle) {
        return searchService.searchArtistBySong(songTitle);
    }

    @GetMapping("/artists/by-album")
    public List<ArtistResponse> searchArtistByAlbum(@RequestParam String albumTitle) {
        return searchService.searchArtistByAlbum(albumTitle);
    }

    // ----------- Tìm kiếm Album -----------
    @GetMapping("/albums")
    public List<AlbumResponse> searchAlbumsByName(@RequestParam("query") String title) {
        return searchService.searchAlbumsByName(title);
    }

    @GetMapping("/albums/by-artist")
    public List<AlbumResponse> searchAlbumsByArtist(@RequestParam String artistName) {
        return searchService.searchAlbumsByArtist(artistName);
    }

    // Search all
    @GetMapping("/all")
    public SearchResponse searchAll(@RequestParam String keyword) {
        return searchService.searchAll(keyword);
    }

}
