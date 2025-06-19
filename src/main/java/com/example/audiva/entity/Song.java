package com.example.audiva.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;

    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    private Integer duration;

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
