package com.itson.profeco.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.api.dto.response.InconsistencyResponse;
import com.itson.profeco.service.InconsistencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("api/v1/store-admin/inconsistencies")
@RequiredArgsConstructor
@Tag(name = "Store Admin Inconsistencies",
        description = "Inconsistency operations for Store Admins")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole(@environment.getProperty('role.store-admin'))")
public class StoreAdminInconsistencyController {

    private final InconsistencyService inconsistencyService;

    @GetMapping
    @Operation(summary = "Get inconsistencies related to the current store admin's store")
    public ResponseEntity<List<InconsistencyResponse>> getInconsistenciesByCurrentStoreAdmin() {
        return ResponseEntity.ok(inconsistencyService.getInconsistenciesByCurrentStoreAdmin());
    }

    @GetMapping("/{inconsistencyId}")
    @Operation(summary = "Get a specific inconsistency by ID (if authorized)")
    public ResponseEntity<InconsistencyResponse> getInconsistencyById(
            @PathVariable UUID inconsistencyId) {
        return ResponseEntity.ok(inconsistencyService.getInconsistencyById(inconsistencyId));
    }
}
