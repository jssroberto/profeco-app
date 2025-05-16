package com.itson.profeco.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.api.dto.request.InconsistencyRequest;
import com.itson.profeco.api.dto.response.InconsistencyResponse;
import com.itson.profeco.service.InconsistencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("api/v1/customer/inconsistencies")
@RequiredArgsConstructor
@Tag(name = "Customer Inconsistencies", description = "Inconsistency operations for Customers")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole(@environment.getProperty('role.customer'))")
public class CustomerInconsistencyController {

    private final InconsistencyService inconsistencyService;

    @GetMapping
    @Operation(summary = "Get inconsistencies reported by the current customer")
    public ResponseEntity<List<InconsistencyResponse>> getInconsistenciesByCurrentCustomer(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(inconsistencyService.getInconsistenciesByCurrentCustomer());
    }

    @PostMapping
    @Operation(summary = "Report a new inconsistency")
    public ResponseEntity<InconsistencyResponse> saveInconsistency(
            @Valid @RequestBody InconsistencyRequest request) {
        InconsistencyResponse response = inconsistencyService.saveInconsistency(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
}
