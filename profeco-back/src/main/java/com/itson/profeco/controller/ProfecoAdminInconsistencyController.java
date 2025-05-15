package com.itson.profeco.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.api.dto.request.UpdateInconsistencyStatusRequest;
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
    @Operation(summary = "Get all inconsistencies (for Profeco Admin)")
    public ResponseEntity<List<InconsistencyResponse>> getAllInconsistencies() {
        return ResponseEntity.ok(inconsistencyService.getAllInconsistenciesForProfecoAdmin());
    }

    @GetMapping("/{inconsistencyId}")
    @Operation(summary = "Get a specific inconsistency by ID (for Profeco Admin)")
    public ResponseEntity<InconsistencyResponse> getInconsistencyById(
            @PathVariable UUID inconsistencyId) {
        return ResponseEntity.ok(inconsistencyService.getInconsistencyById(inconsistencyId));
    }

    @GetMapping("/store/{storeId}")
    @Operation(summary = "Get all inconsistencies for a specific store ID (for Profeco Admin)")
    public ResponseEntity<List<InconsistencyResponse>> getInconsistenciesByStoreId(
            @PathVariable UUID storeId) {
        return ResponseEntity.ok(inconsistencyService.getInconsistenciesByStore(storeId));
    }

    @PatchMapping("/{inconsistencyId}/status")
    @Operation(summary = "Update the status of an inconsistency (for Profeco Admin)")
    public ResponseEntity<InconsistencyResponse> updateInconsistencyStatus(
            @PathVariable UUID inconsistencyId,
            @RequestBody UpdateInconsistencyStatusRequest statusRequest) {
        InconsistencyResponse response =
                inconsistencyService.updateInconsistencyStatus(inconsistencyId, statusRequest);
        return ResponseEntity.ok(response);
    }
}
