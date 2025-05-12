package com.itson.profeco.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.itson.profeco.api.dto.response.AuthResponse;
import com.itson.profeco.security.CustomUserDetails;
import com.itson.profeco.security.JwtUtil;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;
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
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @GetMapping("/me")
    public ResponseEntity<CustomerResponse> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        CustomerResponse customer = customerService.getCurrentCustomer();
        return ResponseEntity.ok(customer);
    }

    @Operation(summary = "Register a new customer",
            description = "Registers a new customer and returns a JWT and user info.")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerCustomer(
            @Valid @RequestBody CustomerRequest request) {
        try {
            customerService.saveCustomer(request);

            CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(request.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(buildAuthResponse(customUserDetails));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already registered: ");
        }

    }


    @Operation(summary = "Update customer", description = "Updates an existing customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")})
    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponse> updateCustomer(@PathVariable UUID id,
                                                           @Valid @RequestBody CustomerRequest customerRequest/*, UriComponentsBuilder uriBuilder*/) { // uriBuilder no es necesario si devuelves 200
        CustomerResponse response = customerService.updateCustomer(id, customerRequest);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Delete customer", description = "Deletes a customer by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    private AuthResponse buildAuthResponse(CustomUserDetails userDetails) {
        String token = jwtUtil.generateToken(userDetails);
        return AuthResponse.builder()
                .accessToken(token)
                .id(userDetails.getSpecificUserId())
                .email(userDetails.getUsername())
                .name(userDetails.getSpecificName() != null ? userDetails.getSpecificName() : "")
                .roles(userDetails.getAuthorities().stream()
                        .map(a -> a.getAuthority().replace("ROLE_", ""))
                        .collect(Collectors.toList()))
                .build();
    }
}