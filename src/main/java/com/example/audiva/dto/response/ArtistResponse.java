package com.example.audiva.dto.response;

import com.example.audiva.entity.Album;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ArtistResponse {
    private Long id;

    private String name;

    private String avatar;

    private List<Album> albums;
}
