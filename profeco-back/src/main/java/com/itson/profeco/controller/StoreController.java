package com.itson.profeco.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.itson.profeco.api.dto.request.StoreRequest;
import com.itson.profeco.api.dto.response.StoreResponse;
import com.itson.profeco.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
@Tag(name = "Store", description = "Operations related to Store")
public class StoreController {

    private final StoreService storeService;

    @Operation(summary = "Get all stores", description = "Returns a list of all stores.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "List of stores returned successfully")})
    @GetMapping
    public ResponseEntity<List<StoreResponse>> getAllStores() {
        List<StoreResponse> responses = storeService.getAllStores();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get store by ID", description = "Returns a store by its ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Store found"),
            @ApiResponse(responseCode = "404", description = "Store not found")})
    @GetMapping("/{id}")
    public ResponseEntity<StoreResponse> getStoreById(@PathVariable UUID id) {
        StoreResponse response = storeService.getStoreById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create store", description = "Creates a new store.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Store created successfully")})
    @PostMapping
    public ResponseEntity<StoreResponse> saveStore(@Valid @RequestBody StoreRequest storeRequest,
            UriComponentsBuilder uriBuilder) {
        StoreResponse response = storeService.saveStore(storeRequest);
        URI location =
                uriBuilder.path("/api/v1/stores/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Update store", description = "Updates an existing store.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "201", description = "Store updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Store not found")})
    @PutMapping("/{id}")
    public ResponseEntity<StoreResponse> updateStore(@PathVariable UUID id,
            @Valid @RequestBody StoreRequest storeRequest, UriComponentsBuilder uriBuilder) {
        StoreResponse response = storeService.updateStore(id, storeRequest);
        URI location = uriBuilder.path("/api/v1/stores/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Delete store", description = "Deletes a store by its ID.")
    @ApiResponses(
            value = {@ApiResponse(responseCode = "204", description = "Store deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Store not found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable UUID id) {
        storeService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }
}
