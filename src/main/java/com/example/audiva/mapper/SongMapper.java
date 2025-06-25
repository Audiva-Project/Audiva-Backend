package com.example.audiva.mapper;

import com.example.audiva.dto.request.SongRequest;
import com.example.audiva.dto.response.SongResponse;
import com.example.audiva.entity.Album;
import com.example.audiva.entity.Artist;
import com.example.audiva.entity.Song;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface SongMapper {

    // Entity -> Response
    @Mapping(source = "album", target = "album")
    @Mapping(source = "artists", target = "artists")
    SongResponse toSongResponse(Song song);

    // Mapping album entity to album summary
    default SongResponse.AlbumSummary mapAlbum(Album album) {
        if (album == null) return null;
        SongResponse.AlbumSummary summary = new SongResponse.AlbumSummary();
        summary.setId(album.getId());
        summary.setTitle(album.getTitle());
        return summary;
    }

    // Mapping artist list to summary list
    default List<SongResponse.ArtistSummary> mapArtists(List<Artist> artists) {
        if (artists == null) return null;
        return artists.stream().map(artist -> {
            SongResponse.ArtistSummary summary = new SongResponse.ArtistSummary();
            summary.setId(artist.getId());
            summary.setName(artist.getName());
            return summary;
        }).collect(Collectors.toList());
    }

    // Request -> Entity (khi tạo mới)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "album", ignore = true)
    @Mapping(target = "artists", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "favoriteSongs", ignore = true)
    @Mapping(target = "listeningHistories", ignore = true)
    @Mapping(target = "playlistSongs", ignore = true)
    Song toSong(SongRequest request);

    // Update entity từ request
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "album", ignore = true)
    @Mapping(target = "artists", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "favoriteSongs", ignore = true)
    @Mapping(target = "listeningHistories", ignore = true)
    @Mapping(target = "playlistSongs", ignore = true)
    void updateSongFromRequest(SongRequest request, @MappingTarget Song song);
}
