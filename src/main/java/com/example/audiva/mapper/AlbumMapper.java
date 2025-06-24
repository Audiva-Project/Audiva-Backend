package com.example.audiva.mapper;

import com.example.audiva.dto.request.AlbumRequest;
import com.example.audiva.dto.response.AlbumResponse;
import com.example.audiva.entity.Album;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AlbumMapper {
    @Mapping(source = "artist.id", target = "artistId")
    @Mapping(source = "artist.name", target = "artistName")
    AlbumResponse toAlbumResponse(Album album);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "songs", ignore = true)
    Album toAlbum(AlbumRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "songs", ignore = true)
    void updateAlbumFromRequest(AlbumRequest request, @MappingTarget Album album);
}
