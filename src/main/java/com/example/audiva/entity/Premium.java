package com.example.audiva.entity;

import com.example.audiva.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "premium")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Premium extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Integer duration;
    private Integer price;

    @OneToMany(mappedBy = "premium", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPremium> userSubscriptions;

    @OneToMany(mappedBy = "premium", fetch = FetchType.LAZY)
    private List<Song> songs;
}

