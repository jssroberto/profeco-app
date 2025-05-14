package com.itson.profeco.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.itson.profeco.api.dto.request.CustomerRequest;
import com.itson.profeco.api.dto.response.CustomerResponse;
import com.itson.profeco.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Operations related to Customer")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/me")
    public ResponseEntity<CustomerResponse> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        CustomerResponse customer = customerService.getCurrentCustomer();
        return ResponseEntity.ok(customer);
    }

    @Operation(summary = "Update current customer", description = "Updates the currently authenticated customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")})
    @PutMapping("/me")
    public ResponseEntity<CustomerResponse> updateCurrentCustomer(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody CustomerRequest customerRequest) {
        CustomerResponse response = customerService.updateCurrentCustomer(customerRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete current customer", description = "Deletes the currently authenticated customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")})
    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCurrentCustomer(
            @AuthenticationPrincipal UserDetails userDetails) {
        customerService.deleteCurrentCustomer();
        return ResponseEntity.noContent().build();
    }
}