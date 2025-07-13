package com.example.audiva.service;

import com.example.audiva.dto.response.AlbumResponse;
import com.example.audiva.dto.response.ArtistResponse;
import com.example.audiva.dto.response.SearchResponse;
import com.example.audiva.dto.response.SongResponse;
import com.example.audiva.entity.Album;
import com.example.audiva.entity.Artist;
import com.example.audiva.entity.Song;
import com.example.audiva.mapper.AlbumMapper;
import com.example.audiva.mapper.ArtistMapper;
import com.example.audiva.mapper.SongMapper;
import com.example.audiva.repository.AlbumRepository;
import com.example.audiva.repository.ArtistRepository;
import com.example.audiva.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SongRepository songRepository;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;

    private final SongMapper songMapper;
    private final ArtistMapper artistMapper;
    private final AlbumMapper albumMapper;

    // ----------- Tìm kiếm Bài hát -----------
    public List<SongResponse> searchSongsByName(String keyword) {
        return songMapper.toSongResponseList(
                songRepository.findByTitleContainingIgnoreCase(keyword)
        );
    }

    public List<SongResponse> searchSongsByArtist(String artistName) {
        return songMapper.toSongResponseList(
                songRepository.findByArtists_NameContainingIgnoreCase(artistName)
        );
    }

    public List<SongResponse> searchSongsByAlbum(String albumName) {
        return songMapper.toSongResponseList(
                songRepository.findByAlbum_TitleContainingIgnoreCase(albumName)
        );
    }

    // ----------- Tìm kiếm Nghệ sĩ -----------
    public List<ArtistResponse> searchArtistsByName(String name) {
        return artistMapper.toArtistResponseList(
                artistRepository.findByNameContainingIgnoreCase(name)
        );
    }

    public ArtistResponse searchArtistBySong(String songTitle) {
        Artist artist = artistRepository.findArtistBySongTitle(songTitle);
        return artist != null ? artistMapper.toArtistResponse(artist) : null;
    }

    public List<ArtistResponse> searchArtistByAlbum(String albumTitle) {
        return artistMapper.toArtistResponseList(
                artistRepository.findArtistByAlbumTitle(albumTitle)
        );
    }

    // ----------- Tìm kiếm Album -----------
    public List<AlbumResponse> searchAlbumsByName(String title) {
        return albumMapper.toAlbumResponseList(
                albumRepository.findByTitleContainingIgnoreCase(title)
        );
    }

    public List<AlbumResponse> searchAlbumsByArtist(String artistName) {
        return albumMapper.toAlbumResponseList(
                albumRepository.searchAlbumsByArtistName(artistName)
        );
    }

    // ----------- Tìm kiếm Tất cả -----------
    public SearchResponse searchAll(String keyword) {
        // --- 1. Songs ---
        List<Song> songsByTitle = songRepository.findByTitleContainingIgnoreCase(keyword);

        List<Artist> artistsByName = artistRepository.findByNameContainingIgnoreCase(keyword);
        List<Song> songsByArtistName = new ArrayList<>();
        artistsByName.forEach(artist -> songsByArtistName.addAll(artist.getSongs()));

        Set<Song> uniqueSongs = new HashSet<>();
        uniqueSongs.addAll(songsByTitle);
        uniqueSongs.addAll(songsByArtistName);

        List<SongResponse> songs = uniqueSongs.stream()
                .map(songMapper::toSongResponse)
                .toList();

        // --- 2. Albums ---
        List<Album> albumsByName = albumRepository.findByTitleContainingIgnoreCase(keyword);
        List<Album> albumsByArtist = albumRepository.findByArtist_NameContainingIgnoreCase(keyword);
        List<Album> albumsBySongTitle = albumRepository.findDistinctBySongs_TitleContainingIgnoreCase(keyword);

        Set<Album> uniqueAlbums = new HashSet<>();
        uniqueAlbums.addAll(albumsByName);
        uniqueAlbums.addAll(albumsByArtist);
        uniqueAlbums.addAll(albumsBySongTitle);

        List<AlbumResponse> albums = uniqueAlbums.stream()
                .map(albumMapper::toAlbumResponse)
                .toList();

        // --- 3. Artists ---
        List<Artist> artistsBySong = artistRepository.findDistinctBySongs_TitleContainingIgnoreCase(keyword);
        Set<Artist> uniqueArtists = new HashSet<>();
        uniqueArtists.addAll(artistsByName);
        uniqueArtists.addAll(artistsBySong);

        List<ArtistResponse> artistResponses = uniqueArtists.stream()
                .map(artistMapper::toArtistResponse)
                .toList();

        return new SearchResponse(songs, artistResponses, albums);
    }
}
