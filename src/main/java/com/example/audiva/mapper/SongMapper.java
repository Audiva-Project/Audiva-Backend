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
    @Mapping(source = "playCount", target = "playCount")
//    @Mapping(source = "album", target = "album")
//    @Mapping(source = "artists", target = "artists")
    SongResponse toSongResponse(Song song);

    @Mapping(target = "listeningHistories", ignore = true)
    @Mapping(target = "playlistSongs", ignore = true)
    Song toSong(SongRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSongFromRequest(SongRequest request, @MappingTarget Song song);

    default SongResponse.AlbumSummary toAlbumSummary(Album album) {
        if (album == null) return null;
        SongResponse.AlbumSummary summary = new SongResponse.AlbumSummary();
        summary.setId(album.getId());
        summary.setTitle(album.getTitle());
        return summary;
    }

    default List<SongResponse.ArtistSummary> toArtistSummaries(List<Artist> artists) {
        if (artists == null) return null;
        return artists.stream().map(this::toArtistSummary).collect(Collectors.toList());
    }

    default SongResponse.ArtistSummary toArtistSummary(Artist artist) {
        return SongResponse.ArtistSummary.builder()
                .id(artist.getId())
                .name(artist.getName())
                .build();
    }

    List<SongResponse> toSongResponseList(List<Song> songs);
}
