package com.example.audiva.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "playlist_song")
public class PlaylistSong extends BaseEntity {
    @EmbeddedId
    private PlaylistSongId id;

    @ManyToOne
    @MapsId("playlistId")
    @JoinColumn(name = "playlist_id")
    private Playlist playlist;

    @ManyToOne
    @MapsId("songId")
    @JoinColumn(name = "song_id")
    private Song song;

    @Column(name = "order_in_playlist")
    private Integer orderInPlaylist;
}

@Embeddable
@Data
class PlaylistSongId implements Serializable {
    private Long playlistId;
    private Long songId;
}