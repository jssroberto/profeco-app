package com.itson.profeco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.api.dto.response.ProfecoAdminResponse;
import com.itson.profeco.service.ProfecoAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/profeco-admins")
@RequiredArgsConstructor
@Tag(name = "Profeco Admin", description = "Operations related to Profeco Administrators")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole(@environment.getProperty('role.profeco-admin'))")
public class ProfecoAdminController {

    private final ProfecoAdminService profecoAdminService;

    @Operation(summary = "Get current Profeco Admin",
            description = "Retrieves the currently authenticated Profeco administrator's details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved Profeco Admin details"),
            @ApiResponse(responseCode = "404", description = "Profeco Admin not found")})
    @GetMapping("/me")
    public ResponseEntity<ProfecoAdminResponse> getCurrentProfecoAdmin(
            @AuthenticationPrincipal UserDetails userDetails) {
        ProfecoAdminResponse response = profecoAdminService.getCurrentProfecoAdmin();
        return ResponseEntity.ok(response);
    }

}
