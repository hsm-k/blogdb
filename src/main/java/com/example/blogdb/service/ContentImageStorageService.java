package com.example.blogdb.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ContentImageStorageService {
    private final Path uploadDirectory;

    public ContentImageStorageService(@Value("${content.upload-dir:uploads/content}") String uploadDirectory) {
        this.uploadDirectory = Path.of(uploadDirectory).toAbsolutePath().normalize();
    }

    public String store(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일을 선택해야 합니다.");
        }

        if (file.getContentType() == null || !file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        String filename = UUID.randomUUID() + "_" + StringUtils.cleanPath(file.getOriginalFilename());

        try {
            Files.createDirectories(uploadDirectory);
            Files.copy(file.getInputStream(), uploadDirectory.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/content/" + filename;
        } catch (IOException e) {
            throw new IllegalStateException("이미지 파일을 저장하지 못했습니다.", e);
        }
    }

    public String getResourceLocation() {
        return uploadDirectory.toUri().toString();
    }
}
