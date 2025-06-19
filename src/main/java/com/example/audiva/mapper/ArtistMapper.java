package com.example.audiva.mapper;

import com.example.audiva.dto.request.ArtistRequest;
import com.example.audiva.dto.response.ArtistResponse;
import com.example.audiva.entity.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ArtistMapper {
    Artist toArtist(ArtistRequest artistRequest);

    ArtistResponse toArtistResponse(Artist artist);

    @Mapping(target = "id", ignore = true)
    void updateArtistFromRequest(ArtistRequest request, @MappingTarget Artist artist);
}
