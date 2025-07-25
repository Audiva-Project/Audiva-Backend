package com.example.audiva.mapper;

import com.example.audiva.dto.request.AlbumRequest;
import com.example.audiva.dto.response.AlbumResponse;
import com.example.audiva.entity.Album;
import com.example.audiva.entity.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {SongMapper.class})
public interface AlbumMapper {

    @Mapping(target = "artist", ignore = true)
    @Mapping(target = "thumbnailUrl", ignore = true)
    Album toAlbum(AlbumRequest request);

    @Mapping(target = "songs", source = "songs")
    @Mapping(target = "artist",
            expression = "java(album.getArtist() != null ? toArtistSummary(album.getArtist()) : null)")
    AlbumResponse toAlbumResponse(Album album);

    default AlbumResponse.ArtistSummary toArtistSummary(Artist artist) {
        return AlbumResponse.ArtistSummary.builder()
                .id(artist.getId())
                .name(artist.getName())
                .build();
    }

    List<AlbumResponse> toAlbumResponseList(List<Album> albums);
}