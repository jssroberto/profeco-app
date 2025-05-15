package com.itson.profeco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.api.dto.response.StoreAdminResponse;
import com.itson.profeco.service.StoreAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/store-admins")
@RequiredArgsConstructor
@Tag(name = "Store Admin", description = "Operations related to Store Administrators")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole(@environment.getProperty('role.store-admin'))")
public class StoreAdminController {

    private final StoreAdminService storeAdminService;

    @Operation(summary = "Get current Store Admin",
            description = "Retrieves the currently authenticated Store administrator's details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved Store Admin details"),
            @ApiResponse(responseCode = "404",
                    description = "Store Admin not found for the authenticated user")})
    @GetMapping("/me")
    public ResponseEntity<StoreAdminResponse> getCurrentStoreAdmin(
            @AuthenticationPrincipal UserDetails userDetails) {
        StoreAdminResponse response = storeAdminService.getCurrentStoreAdmin();
        return ResponseEntity.ok(response);
    }

}
