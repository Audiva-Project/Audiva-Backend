package com.example.audiva.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "song-lyrics")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Lyrics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "song_id")
    private Song song;

    @Lob
    private String content;
}
