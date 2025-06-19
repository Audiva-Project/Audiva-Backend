package com.example.audiva.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "favorite_song")
public class FavoriteSong extends BaseEntity {
    @EmbeddedId
    private FavoriteSongId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("songId")
    @JoinColumn(name = "song_id")
    private Song song;
}

@Embeddable
@Data
class FavoriteSongId implements Serializable {
    private Long userId;
    private Long songId;
}