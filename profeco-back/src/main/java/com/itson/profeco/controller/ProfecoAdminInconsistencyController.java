package com.itson.profeco.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.itson.profeco.api.dto.response.InconsistencyResponse;
import com.itson.profeco.service.InconsistencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("api/v1/profeco-admin/inconsistencies")
@RequiredArgsConstructor
@Tag(name = "Profeco Admin Inconsistencies",
        description = "Inconsistency operations for Profeco Admins")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole(@environment.getProperty('role.profeco-admin'))")
public class ProfecoAdminInconsistencyController {

    private final InconsistencyService inconsistencyService;

    @GetMapping
    @Operation(summary = "Get all inconsistencies")
    public ResponseEntity<List<InconsistencyResponse>> getAllInconsistencies() {
        List<InconsistencyResponse> inconsistencyResponses =
                inconsistencyService.getAllInconsistenciesForProfecoAdmin();
        return ResponseEntity.ok(inconsistencyResponses);
    }

    @GetMapping("/{inconsistencyId}")
    @Operation(summary = "Get a specific inconsistency by ID")
    public ResponseEntity<InconsistencyResponse> getInconsistencyById(
            @PathVariable UUID inconsistencyId) {
        InconsistencyResponse inconsistencyResponse =
                inconsistencyService.getInconsistencyById(inconsistencyId);
        return ResponseEntity.ok(inconsistencyResponse);
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "Get all inconsistencies for a specific store ID")
    public ResponseEntity<List<InconsistencyResponse>> getInconsistenciesByStoreId(
            @PathVariable UUID storeId) {
        List<InconsistencyResponse> inconsistencyResponses =
                inconsistencyService.getInconsistenciesByStore(storeId);
        return ResponseEntity.ok(inconsistencyResponses);
    }

    @PatchMapping("/{inconsistencyId}/close")
    @Operation(summary = "Update the status of an inconsistency to CLOSED")
    public ResponseEntity<InconsistencyResponse> closeInconsistency(
            @PathVariable UUID inconsistencyId) {
        InconsistencyResponse inconsistencyResponse =
                inconsistencyService.updateInconsistencyStatus(inconsistencyId);
        URI resourceUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/api/v1/profeco-admin/inconsistencies/{id}")
                .buildAndExpand(inconsistencyId).toUri();
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_LOCATION, resourceUri.toString())
                .body(inconsistencyResponse);
    }
}
