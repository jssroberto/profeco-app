package com.itson.profeco.controller;

import com.itson.profeco.api.dto.request.StoreProductRequest;
import com.itson.profeco.api.dto.response.StoreProductResponse;
import com.itson.profeco.service.StoreProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/store-products")
@RequiredArgsConstructor
@Tag(name = "Store Product Management", description = "APIs for managing products within stores")
public class StoreProductController {

    private final StoreProductService storeProductService;

    @Operation(summary = "Create a new store product", description = "Adds a new product listing to a store with its price.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Store product created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required permissions (e.g., ROLE_ADMIN)")
    })
    @PostMapping
    @PreAuthorize("hasRole('STORE_ADMIN')")
    public ResponseEntity<StoreProductResponse> createStoreProduct(@Valid @RequestBody StoreProductRequest request) {
        StoreProductResponse response = storeProductService.createStoreProduct(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a store product by ID", description = "Retrieves a specific store product by its unique identifier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved store product",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreProductResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Store product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StoreProductResponse> getStoreProductById(
            @Parameter(description = "ID of the store product to retrieve") @PathVariable UUID id) {
        return ResponseEntity.ok(storeProductService.getStoreProductById(id));
    }

    @Operation(summary = "Update an existing store product", description = "Updates details of an existing store product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store product updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required permissions"),
            @ApiResponse(responseCode = "404", description = "Store product not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STORE_ADMIN')")
    public ResponseEntity<StoreProductResponse> updateStoreProduct(
            @Parameter(description = "ID of the store product to update") @PathVariable UUID id,
            @Valid @RequestBody StoreProductRequest request) {
        return ResponseEntity.ok(storeProductService.updateStoreProduct(id, request));
    }

    @Operation(summary = "Delete a store product", description = "Removes a store product listing.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Store product deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required permissions"),
            @ApiResponse(responseCode = "404", description = "Store product not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STORE_ADMIN')")
    public ResponseEntity<Void> deleteStoreProduct(
            @Parameter(description = "ID of the store product to delete") @PathVariable UUID id) {
        storeProductService.deleteStoreProduct(id);
        return ResponseEntity.noContent().build();
    }


    @Operation(summary = "Find store products by store ID", description = "Retrieves all products associated with a specific store ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products for the store"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/by-store/{storeId}")
    public ResponseEntity<List<StoreProductResponse>> getProductsByStoreId(
            @Parameter(description = "ID of the store") @PathVariable UUID storeId) {
        return ResponseEntity.ok(storeProductService.getProductsByStoreId(storeId));
    }

    @Operation(summary = "Find store products by store name", description = "Retrieves products by the name of the store.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/by-store-name")
    public ResponseEntity<List<StoreProductResponse>> getProductsByStoreName(
            @Parameter(description = "Name of the store") @RequestParam String name) {
        return ResponseEntity.ok(storeProductService.getProductsByStoreName(name));
    }

    @Operation(summary = "Find store products by product name", description = "Retrieves products by their name across all stores.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/by-product-name")
    public ResponseEntity<List<StoreProductResponse>> getProductsByProductName(
            @Parameter(description = "Name of the product") @RequestParam String name) {
        return ResponseEntity.ok(storeProductService.getProductsByProductName(name));
    }

    @Operation(summary = "Find store products by price range", description = "Retrieves products within a specified price range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/by-price-range")
    public ResponseEntity<List<StoreProductResponse>> getProductsByPriceRange(
            @Parameter(description = "Minimum price") @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum price") @RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(storeProductService.getProductsByPriceBetween(minPrice, maxPrice));
    }


    @Operation(summary = "Find store product by store and product IDs", description = "Retrieves a specific product for a specific store.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved store product"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Store product not found")
    })
    @GetMapping("/by-store/{storeId}/product/{productId}")
    public ResponseEntity<StoreProductResponse> getProductByStoreAndProductIds(
            @Parameter(description = "ID of the store") @PathVariable UUID storeId,
            @Parameter(description = "ID of the product") @PathVariable UUID productId) {
        return ResponseEntity.ok(storeProductService.getProductByStoreAndProductIds(storeId, productId));
    }

    @Operation(summary = "Find store products with valid prices for a specific store", description = "Retrieves products with valid (non-null or positive offer) prices, ordered by base price.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @GetMapping("/by-store/{storeId}/with-valid-prices")
    public ResponseEntity<List<StoreProductResponse>> findStoreProductsWithValidPrices(
            @Parameter(description = "ID of the store") @PathVariable UUID storeId) {
        return ResponseEntity.ok(storeProductService.findStoreProductsWithValidPrices(storeId));
    }
}