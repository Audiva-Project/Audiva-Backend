package com.example.audiva.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SongRequest {
    String title;
    String genre;
    Integer duration;
    MultipartFile audioFile;
    MultipartFile thumbnailFile;
    String isPremium;
    Long albumId;
    List<Long> artistIds;
}
