package com.example.audiva.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PlaylistRequest {
    private String name;
    private String description;
    private MultipartFile thumbnailFile;
}
