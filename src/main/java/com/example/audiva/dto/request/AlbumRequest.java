package com.example.audiva.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
public class AlbumRequest {
    private String title;
    private LocalDate releaseDate;
    private Long artistId;
    private MultipartFile thumbnailFile;
    private List<Long> songIds;
}
