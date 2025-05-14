package com.itson.profeco.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.PostConstruct;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private Path rootLocation;

    @PostConstruct
    public void init() {
        try {
            this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location", e);
        }
    }

    public String store(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        String rawOriginalFilename = file.getOriginalFilename();
        if (rawOriginalFilename == null) {
            throw new RuntimeException("Failed to store file: original filename cannot be null.");
        }
        String originalFilename = StringUtils.cleanPath(rawOriginalFilename);
        String fileExtension = "";
        try {
            if (originalFilename.contains("..")) {
                throw new RuntimeException(
                        "Cannot store file with relative path outside current directory "
                                + originalFilename);
            }
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = originalFilename.substring(dotIndex);
            }

            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            Path destinationFile = this.rootLocation.resolve(Paths.get(uniqueFilename)).normalize()
                    .toAbsolutePath();

            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                throw new RuntimeException(
                        "Cannot store file outside designated directory " + uniqueFilename);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }
            return uniqueFilename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + originalFilename, e);
        }
    }

    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename).normalize().toAbsolutePath();

            if (!file.startsWith(this.rootLocation.toAbsolutePath())) {
                throw new RuntimeException("Cannot access file outside designated directory.");
            }

            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not read file: " + filename, e);
        }
    }

}
