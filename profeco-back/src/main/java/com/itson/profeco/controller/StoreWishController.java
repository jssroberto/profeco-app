package com.itson.profeco.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.api.dto.response.WishResponse;
import com.itson.profeco.service.StoreAdminService;
import com.itson.profeco.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/supermarket/wishes")
@RequiredArgsConstructor
@Tag(name = "Store Wish", description = "Operations for Supermarkets on Wishes in their Store")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('STORE-ADMIN')")
public class StoreWishController {

    private final WishService wishService;
    private final StoreAdminService storeAdminService;

    private UUID getCurrentStoreId() {
        return storeAdminService.getCurrentStoreAdmin().getStoreId();
    }

    @Operation(summary = "Get all wishes for the supermarket's store",
            description = "Retrieves wishes associated with the store managed by the authenticated supermarket user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved wish list for the store"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - User does not have SUPERMARKET role or is not associated with a store")})
    @GetMapping
    public ResponseEntity<List<WishResponse>> getWishesForSupermarketStore() {
        UUID storeId = getCurrentStoreId();
        List<WishResponse> wishes = wishService.getAllWishesForStore(storeId);
        return ResponseEntity.ok(wishes);
    }

    @Operation(summary = "Get a specific wish by ID for the supermarket's store",
            description = "Retrieves a specific wish if it belongs to the store managed by the authenticated supermarket user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved wish"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - User does not have SUPERMARKET role or is not associated with a store"),
            @ApiResponse(responseCode = "404",
                    description = "Wish not found or does not belong to this supermarket's store (handled by service)")})
    @GetMapping("/{wishId}")
    public ResponseEntity<WishResponse> getWishByIdForStore(
            @Parameter(description = "ID of the wish to retrieve",
                    required = true) @PathVariable UUID wishId) {
        UUID storeId = getCurrentStoreId();
        WishResponse wish = wishService.getWishByIdForStore(wishId, storeId);
        return ResponseEntity.ok(wish);
    }

    @Operation(summary = "Delete a specific wish from the supermarket's store",
            description = "Deletes a wish if it belongs to the store managed by the authenticated supermarket user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Wish deleted successfully from the store"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - User does not have SUPERMARKET role or is not associated with a store"),
            @ApiResponse(responseCode = "404",
                    description = "Wish not found or does not belong to this supermarket's store (handled by service)")})
    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> deleteWishFromSupermarketStore(@Parameter(
            description = "ID of the wish to delete", required = true) @PathVariable UUID wishId) {
        UUID storeId = getCurrentStoreId();
        wishService.deleteWishForStore(wishId, storeId);
        return ResponseEntity.noContent().build();
    }
}
