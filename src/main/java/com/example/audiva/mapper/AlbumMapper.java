package com.example.audiva.mapper;

import com.example.audiva.dto.request.AlbumRequest;
import com.example.audiva.dto.response.AlbumResponse;
import com.example.audiva.entity.Album;
import com.example.audiva.entity.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "thumbnailUrl", ignore = true)
    Album toAlbum(AlbumRequest request);

    @Mapping(source = "artist.id", target = "artistId")
    @Mapping(source = "artist.name", target = "artistName")
    @Mapping(target = "songs", source = "songs")
    AlbumResponse toAlbumResponse(Album album);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "thumbnailUrl", ignore = true)
    void updateAlbumFromRequest(AlbumRequest request, @MappingTarget Album album);

    @Named("songIdsFromSongs")
    default List<Long> songIdsFromSongs(List<Song> songs) {
        if (songs == null) return null;
        return songs.stream()
                .map(Song::getId)
                .toList();
    }
}