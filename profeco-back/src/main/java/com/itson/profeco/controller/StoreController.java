package com.itson.profeco.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.itson.profeco.api.dto.request.StoreRequest;
import com.itson.profeco.api.dto.response.StoreResponse;
import com.itson.profeco.service.FileStorageService;
import com.itson.profeco.service.StoreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@Tag(name = "Store", description = "Operations related to Store")
@SecurityRequirement(name = "bearerAuth")
public class StoreController {

    private final StoreService storeService;

    private final FileStorageService fileStorageService;

    @Operation(summary = "Get all stores", description = "Returns a list of all stores.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "List of stores returned successfully") })
    @GetMapping
   @PreAuthorize("hasAnyRole(@environment.getProperty('role.customer'), @environment.getProperty('role.store-admin'))")
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        List<StoreResponse> responses = storeService.getAllStores();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get store by ID", description = "Returns a store by its ID.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Store found"),
            @ApiResponse(responseCode = "404", description = "Store not found") })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole(@environment.getProperty('role.customer'), @environment.getProperty('role.store-admin'))")
    public ResponseEntity<StoreResponse> getStoreById(@PathVariable UUID id) {
        StoreResponse response = storeService.getStoreById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create store", description = "Creates a new store.", requestBody = @RequestBody(content = @Content(encoding = {
            @Encoding(name = "storeRequest", contentType = MediaType.APPLICATION_JSON_VALUE),
            @Encoding(name = "image") })))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Store created successfully") })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole(@environment.getProperty('role.store-admin'))")
    public ResponseEntity<StoreResponse> saveStore(@Valid @RequestPart StoreRequest storeRequest,
            @RequestPart MultipartFile image, UriComponentsBuilder uriBuilder) {

        String uniqueFilename = fileStorageService.store(image);
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/images/").path(uniqueFilename).toUriString();

        StoreResponse response = storeService.saveStore(storeRequest, imageUrl);

        URI location = uriBuilder.path("/api/v1/stores/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

}
