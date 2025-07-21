package com.example.audiva.controller;

import com.example.audiva.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/audio")
public class AudioController {

    private final UploadService uploadService;

    // 1. Upload file MP3
    @PostMapping("/upload")
    public ResponseEntity<String> uploadAudio(@RequestParam("file") MultipartFile file) {
        try {
            String savedFilename = uploadService.uploadFile(file);
            return ResponseEntity.ok("File uploaded as: " + savedFilename);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Upload failed: " + e.getMessage());
        }
    }

    // 2. Get file URL by filename
    @GetMapping("/{filename:.+}")
    public ResponseEntity<String> getFile(@PathVariable String filename) {
        try {
            String fileUrl = uploadService.getFileUrl(filename);
            return ResponseEntity.ok(fileUrl);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
