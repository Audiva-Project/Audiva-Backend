package com.example.audiva.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.audiva.exception.AppException;
import com.example.audiva.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.text.Normalizer;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final Cloudinary cloudinary;

    private final Path mp3Root = Paths.get(System.getProperty("user.dir"), "uploads/mp3");
    private final Path imageRoot = Paths.get(System.getProperty("user.dir"), "uploads/picture");

    public String uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IOException("Invalid file name");
        }

        String publicId = buildPublicId(originalFilename);

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap(
                        "resource_type", "auto",
                        "public_id", publicId
                ));

        return (String) uploadResult.get("secure_url");
    }

    public String getFileUrl(String filename) {
        try {
            String publicId = buildPublicId(filename);

            Map result = cloudinary.uploader().explicit(publicId,
                    ObjectUtils.asMap("resource_type", "auto", "type", "upload"));

            return (String) result.get("secure_url");
        } catch (Exception e) {
            try {
                Path file = loadByFilename(filename);
                if (Files.exists(file)) {
                    String publicId = buildPublicId(filename);

                    Map uploadResult = cloudinary.uploader().upload(file.toFile(),
                            ObjectUtils.asMap(
                                    "resource_type", "auto",
                                    "public_id", publicId
                            ));

                    return (String) uploadResult.get("secure_url");
                } else {
                    throw new AppException(ErrorCode.FILE_NOT_FOUND);
                }
            } catch (IOException ex) {
                throw new AppException(ErrorCode.FILE_NOT_FOUND);
            }
        }
    }

    public String convertToSlug(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        String slug = normalized.replaceAll("\\p{M}", "");
        return slug.toLowerCase()
                .replaceAll("[^a-z0-9\\s]", "")
                .trim()
                .replaceAll("\\s+", "-");
    }

    private String stripExtension(String filename) {
        int index = filename.lastIndexOf('.');
        return (index == -1) ? filename : filename.substring(0, index);
    }

    private String buildPublicId(String filename) {
        String ext = getFileExtension(filename).toLowerCase();
        String nameWithoutExt = stripExtension(filename);
        String slug = convertToSlug(nameWithoutExt);
        String folder = isAudio(ext) ? "audiva/audio/" : "audiva/img/";
        return folder + slug;
    }

    public Path loadByFilename(String filename) {
        String ext = getFileExtension(filename).toLowerCase();
        if (isImage(ext)) {
            return imageRoot.resolve(filename);
        } else if (isAudio(ext)) {
            return mp3Root.resolve(filename);
        } else {
            throw new AppException(ErrorCode.INVALID_FILE_TYPE);
        }
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
}
