package com.itson.profeco.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.api.dto.request.InconsistencyRequest;
import com.itson.profeco.api.dto.response.InconsistencyResponse;
import com.itson.profeco.service.InconsistencyService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/incosistencies")
@RequiredArgsConstructor
@Tag(name = "Inconsistencies", description = "Operations related to Inconsistencies")
@SecurityRequirement(name = "bearerAuth")
public class InconcistencyController {

    private final InconsistencyService inconsistencyService;

    @GetMapping("/profeco-admin")
    @PreAuthorize("hasRole(@environment.getProperty('role.profeco-admin'))")
    public List<InconsistencyResponse> getAllInconsistencies() {
        return null;
    }

    @GetMapping("/store-admin")
    @PreAuthorize("hasRole(@environment.getProperty('role.store-admin'))")
    public List<InconsistencyResponse> getInconsistenciesByCurrentStoreAdmin() {
        return null;
    }

    @GetMapping("/customer")
    @PreAuthorize("hasRole(@environment.getProperty('role.customer'))")
    public List<InconsistencyResponse> getInconsistenciesByCurrentCustomer() {
        return null;
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole(@environment.getProperty('role.profeco-admin')) or hasRole(@environment.getProperty('role.store-admin')) ")
    public InconsistencyResponse getInconsistencyById() {
        return null;
    }

    @GetMapping("/store/{id}")
    @PreAuthorize("hasRole(@environment.getProperty('role.profeco-admin'))")
    public List<InconsistencyResponse> getInconsistenciesByStoreId() {
        return null;
    }

    @PostMapping()
    @PreAuthorize("hasRole(@environment.getProperty('role.customer'))")
    public InconsistencyResponse saveInconsistency(@RequestBody InconsistencyRequest request) {
        return null;
    }

    @PatchMapping()
    @PreAuthorize("hasRole(@environment.getProperty('role.profeco-admin'))")
    public InconsistencyResponse updateInconsistency(@RequestBody InconsistencyRequest request) {
        return null;
    }
}
