package com.itson.profeco.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.api.dto.request.ProductSearchRequest;
import com.itson.profeco.api.dto.response.FavoriteStoresResponse;
import com.itson.profeco.api.dto.response.ProductSearchResponse;
import com.itson.profeco.api.dto.response.ShoppingListResponse;
import com.itson.profeco.security.CustomUserDetails;
import com.itson.profeco.service.PreferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/preferences")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole(@environment.getProperty('role.customer'))")
@Tag(name = "My Preferences", description = "Manage current authenticated customer's preferences")
public class PreferenceController {

    private final PreferenceService preferenceService;

    @Operation(summary = "Add favorite store",
            description = "Adds a store to the current customer's favorite stores list",
            tags = {"Favorite Stores"})
    @PostMapping("/favorite-stores/{storeId}")
    public ResponseEntity<FavoriteStoresResponse> addFavoriteStore(
            @Parameter(description = "ID of the store to add") @PathVariable UUID storeId) {
        return preferenceService.addFavoriteStore(storeId).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Remove favorite store",
            description = "Removes a store from the current customer's favorite stores list",
            tags = {"Favorite Stores"})
    @DeleteMapping("/favorite-stores/{storeId}")
    public ResponseEntity<FavoriteStoresResponse> removeFavoriteStore(
            @Parameter(description = "ID of the store to remove") @PathVariable UUID storeId) {

        return preferenceService.removeFavoriteStore(storeId).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get searched products",
            description = "Retrieves the current customer's searched products ordered by search count in descending order, with optional limit",
            tags = {"Product Searches"})
    @GetMapping("/product-searches")
    public ResponseEntity<List<ProductSearchResponse>> getProductSearchesAsc() {

        List<ProductSearchResponse> response =
                preferenceService.getAllProductSearchesSortedByCountDesc();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Get favorite stores",
            description = "Retrieves all favorite stores for the current customer",
            tags = {"Favorite Stores"})
    @GetMapping("/favorite-stores")
    public ResponseEntity<FavoriteStoresResponse> getFavoriteStores() {
        return ResponseEntity.ok(preferenceService.getFavoriteStores());
    }

    @Operation(summary = "Add product to shopping list",
            description = "Adds a product to the current customer's shopping list",
            tags = {"Shopping List"})
    @PostMapping("/shopping-list/{productId}")
    public ResponseEntity<ShoppingListResponse> addProductToShoppingList(
            @Parameter(description = "ID of the product to add") @PathVariable UUID productId) {
        return preferenceService.addProductToShoppingList(productId).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Remove product from shopping list",
            description = "Removes a product from the current customer's shopping list",
            tags = {"Shopping List"})
    @DeleteMapping("/shopping-list/{productId}")
    public ResponseEntity<ShoppingListResponse> removeProductFromShoppingList(
            @AuthenticationPrincipal CustomUserDetails currentUser, // Injected authenticated user
            @Parameter(description = "ID of the product to remove") @PathVariable UUID productId) {
        return preferenceService.removeProductFromShoppingList(productId).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Get shopping list",
            description = "Retrieves all products in the current customer's shopping list",
            tags = {"Shopping List"})
    @GetMapping("/shopping-list")
    public ResponseEntity<ShoppingListResponse> getShoppingList() {
        return ResponseEntity.ok(preferenceService.getShoppingList());
    }

    @Operation(summary = "Track product search",
            description = "Records or updates a product search by the current customer",
            tags = {"Product Searches"})
    @PostMapping("/product-searches")
    public ResponseEntity<ProductSearchResponse> trackProductSearch(
            @Valid @RequestBody ProductSearchRequest request) {
        return ResponseEntity.ok(preferenceService.addOrUpdateProductSearch(request));
    }

    @Operation(summary = "Get product searches limited by count",
            description = "Retrieves the current customer's recent product searches, optionally limited by count",
            tags = {"Product Searches"})
    @GetMapping("/product-searches/count")
    public ResponseEntity<List<ProductSearchResponse>> getProductSearches(
            @Parameter(description = "Maximum number of searches to return") @RequestParam(
                    required = false) Integer limit) {

        return ResponseEntity.ok(preferenceService.getProductSearches(Optional.ofNullable(limit)));
    }
}
