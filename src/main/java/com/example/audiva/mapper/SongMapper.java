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
    @Mapping(source = "createdBy", target = "createdBy")
    SongResponse toSongResponse(Song song);

    @Mapping(target = "favoriteSongs", ignore = true)
    @Mapping(target = "listeningHistories", ignore = true)
    @Mapping(target = "playlistSongs", ignore = true)
    Song toSong(SongRequest request);

    @Mapping(target = "favoriteSongs", ignore = true)
    @Mapping(target = "listeningHistories", ignore = true)
    @Mapping(target = "playlistSongs", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSongFromRequest(SongRequest request, @MappingTarget Song song);
}
