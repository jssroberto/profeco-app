package com.itson.profeco.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.itson.profeco.api.dto.request.WishRequest;
import com.itson.profeco.api.dto.response.WishResponse;
import com.itson.profeco.service.CustomerService;
import com.itson.profeco.service.WishService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/customer/wishes")
@RequiredArgsConstructor
@Tag(name = "Customer Wish Management", description = "Operations for Customers on their own Wishes")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole(@environment.getProperty('role.customer'))")
public class CustomerWishController {

    private final WishService wishService;
    private final CustomerService contextService;

    private UUID getCurrentCustomerId() {
        return contextService.getCurrentCustomer().getId();
    }

    @Operation(summary = "Get all wishes for current customer", description = "Retrieves a list of all wishes belonging to the currently authenticated customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved wish list"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid or missing token"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have CUSTOMER role or authentication issue") })
    @GetMapping
    public ResponseEntity<List<WishResponse>> getCurrentCustomerWishes() {
        UUID customerId = getCurrentCustomerId();
        List<WishResponse> wishes = wishService.getAllWishesForCurrentUser(customerId);
        return ResponseEntity.ok(wishes);
    }

    @Operation(summary = "Get a specific wish by ID for current customer", description = "Retrieves a specific wish by its ID, only if it belongs to the currently authenticated customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved wish"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have CUSTOMER role"),
            @ApiResponse(responseCode = "404", description = "Wish not found or does not belong to the user (handled by service)") })
    @GetMapping("/{id}")
    public ResponseEntity<WishResponse> getCurrentCustomerWishById(
            @Parameter(description = "ID of the wish to retrieve", required = true) @PathVariable UUID id) {
        UUID customerId = getCurrentCustomerId();
        WishResponse wish = wishService.getWishByIdForCurrentUser(id, customerId);
        return ResponseEntity.ok(wish);
    }

    @Operation(summary = "Create a new wish for current customer", description = "Adds a new wish associated with the currently authenticated customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Wish created successfully", headers = @Header(name = "Location", description = "URL of the newly created wish")),
            @ApiResponse(responseCode = "400", description = "Invalid input data (validation error)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have CUSTOMER role"),
            @ApiResponse(responseCode = "404", description = "Related Store specified in the request not found (handled by service)") })
    @PostMapping
    public ResponseEntity<WishResponse> createCustomerWish(
            @Valid @RequestBody WishRequest wishRequest) {
        WishResponse savedWish = wishService.createWishForCurrentUser(wishRequest);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedWish.getId()).toUri();

        return ResponseEntity.created(location).body(savedWish);
    }

    @Operation(summary = "Update an existing wish for current customer", description = "Updates the details of an existing wish, only if it belongs to the currently authenticated customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Wish updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data (validation error)"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have CUSTOMER role"),
            @ApiResponse(responseCode = "404", description = "Wish not found (or does not belong to user), or related Store not found (handled by service)") })
    @PutMapping("/{id}")
    public ResponseEntity<WishResponse> updateCustomerWish(
            @Parameter(description = "ID of the wish to update", required = true) @PathVariable UUID id,
            @Valid @RequestBody WishRequest wishRequest) {
        WishResponse updatedWish = wishService.updateWishForCurrentUser(id, wishRequest);
        return ResponseEntity.ok(updatedWish);
    }

    @Operation(summary = "Delete a wish for current customer", description = "Deletes a wish by its ID, only if it belongs to the currently authenticated customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Wish deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have CUSTOMER role"),
            @ApiResponse(responseCode = "404", description = "Wish not found or does not belong to the user (handled by service)") })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomerWish(
            @Parameter(description = "ID of the wish to delete", required = true) @PathVariable UUID id) {
        wishService.deleteWishForCurrentUser(id);
        return ResponseEntity.noContent().build();
    }
}
