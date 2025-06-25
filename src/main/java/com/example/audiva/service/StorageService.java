package com.example.audiva.service;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class StorageService {
    private final Path mp3Root = Paths.get(System.getProperty("user.dir"), "uploads/mp3");
    private final Path imageRoot = Paths.get(System.getProperty("user.dir"), "uploads/picture");

    public StorageService() throws IOException {
        Files.createDirectories(mp3Root);
        Files.createDirectories(imageRoot);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IOException("Invalid file name");
        }

        String filename = System.currentTimeMillis() + "_" + originalFilename;
        String fileExt = getFileExtension(originalFilename).toLowerCase();

        Path destination;

        if (isImage(fileExt)) {
            destination = imageRoot.resolve(filename);
        } else if (isAudio(fileExt)) {
            destination = mp3Root.resolve(filename);
        } else {
            throw new IOException("Unsupported file type: " + fileExt);
        }

        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }

    public Path load(String filename) {
        return mp3Root.resolve(filename);
    }

    private String getFileExtension(String filename) {
        int index = filename.lastIndexOf('.');
        return (index == -1) ? "" : filename.substring(index + 1);
    }

    private boolean isImage(String ext) {
        return ext.matches("(?i)png|jpg|jpeg|gif|bmp|webp");
    }

    private boolean isAudio(String ext) {
        return ext.matches("(?i)mp3|wav|ogg|flac|m4a");
    }

    public Path loadByFilename(String filename) {
        String ext = getFileExtension(filename).toLowerCase();

        if (isImage(ext)) {
            return imageRoot.resolve(filename);
        } else if (isAudio(ext)) {
            return mp3Root.resolve(filename);
        } else {
            throw new IllegalArgumentException("Unsupported file type for loading: " + ext);
        }
    }

    public MediaType getMediaType(String filename) {
        String ext = getFileExtension(filename).toLowerCase();

        switch (ext) {
            case "mp3":
                return MediaType.valueOf("audio/mpeg");
            case "wav":
                return MediaType.valueOf("audio/wav");
            case "ogg":
                return MediaType.valueOf("audio/ogg");
            case "flac":
                return MediaType.valueOf("audio/flac");
            case "m4a":
                return MediaType.valueOf("audio/mp4");
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
            case "jpeg":
                return MediaType.IMAGE_JPEG;
            case "gif":
                return MediaType.IMAGE_GIF;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

}
