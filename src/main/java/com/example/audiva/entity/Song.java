package com.example.audiva.entity;

import com.example.audiva.enums.Genre;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "song")
public class Song extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private Genre genre;

    @Column(name = "audio_url")
    private String audioUrl;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @ManyToOne
    @JoinColumn(name = "album_id")
    @JsonIgnore
    private Album album;

    @OneToMany(mappedBy = "song", fetch = FetchType.LAZY)
    private List<ListeningHistory> listeningHistories;

    @OneToMany(mappedBy = "song", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<PlaylistSong> playlistSongs;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "song_artist",
            joinColumns = @JoinColumn(name = "song_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    @JsonIgnore
    private List<Artist> artists;

    @Column(name = "is_premium", columnDefinition = "BOOLEAN DEFAULT FALSE", nullable = false)
    private boolean premium;

    @Column(name = "play_count")
    private Long playCount = 0L;

    @OneToOne(mappedBy = "song", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Lyrics lyrics;
}