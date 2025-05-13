package com.itson.profeco.controller;

import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.itson.profeco.api.dto.response.ImageResponse;
import com.itson.profeco.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
@Tag(name = "Image", description = "Endpoints for image upload")
public class ImageController {

    private final FileStorageService fileStorageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload an image file",
            description = "Uploads a single image file and returns its filename and URL. The maximum file size is 10MB.")
    public ResponseEntity<ImageResponse> uploadFile(@RequestParam MultipartFile file) {
        
        String uniqueFilename = fileStorageService.store(file);
        String fileDownloadUriString = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/images/").path(uniqueFilename).toUriString();

        ImageResponse response = new ImageResponse(uniqueFilename, fileDownloadUriString);
        URI location = URI.create(fileDownloadUriString);

        return ResponseEntity.created(location).body(response);

    }
}
