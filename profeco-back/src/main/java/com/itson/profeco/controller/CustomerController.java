package com.itson.profeco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.api.dto.response.CustomerResponse;
import com.itson.profeco.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Operations related to Customer")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole(@environment.getProperty('role.customer'))")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Get current customer",
            description = "Retrieves the currently authenticated customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")})
    @GetMapping("/me")
    public ResponseEntity<CustomerResponse> getCurrentCustomer(
            @AuthenticationPrincipal UserDetails userDetails) {
        CustomerResponse customer = customerService.getCurrentCustomer();
        return ResponseEntity.ok(customer);
    }

}
