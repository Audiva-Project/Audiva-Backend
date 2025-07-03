package com.example.audiva.mapper;

import com.example.audiva.dto.response.PlaylistResponse;
import com.example.audiva.dto.response.SongResponse;
import com.example.audiva.entity.Playlist;
import com.example.audiva.entity.PlaylistSong;
import com.example.audiva.entity.Song;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlaylistMapper {

    @Mapping(source = "playlistSongs", target = "songs")
    PlaylistResponse toPlaylistResponse(Playlist playlist);

    @IterableMapping(qualifiedByName = "playlistSongToSongResponse")
    List<SongResponse> map(List<PlaylistSong> playlistSongs);

    @Named("playlistSongToSongResponse")
    default SongResponse map(PlaylistSong playlistSong) {
        return toSongResponse(playlistSong.getSong());
    }

    SongResponse toSongResponse(Song song);
}
