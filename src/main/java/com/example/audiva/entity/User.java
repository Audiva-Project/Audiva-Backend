package com.example.audiva.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dob;

    @ManyToMany(fetch = FetchType.EAGER)
    Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<UserPremium> premiumSubscriptions;

    @OneToMany(mappedBy = "user")
    Set<Playlist> playlists;

    @Column(name = "download_count")
    private Integer downloadCount;

    @Column(name = "last_download_reset_date")
    private LocalDate lastDownloadResetDate;
}

