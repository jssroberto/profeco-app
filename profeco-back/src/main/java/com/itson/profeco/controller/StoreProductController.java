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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Store Product Management (Base Products)", description = "APIs for managing base products (without offers) within stores.")
public class StoreProductController {

    private final StoreProductService storeProductService;

    @Operation(summary = "Create a new store product (base price)",
            description = "Adds a new product listing to a store with its base price. Offers are managed separately.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Store product created successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have STORE_ADMIN role")
    })
    @PostMapping
    @PreAuthorize("hasRole('STORE_ADMIN')")
    public ResponseEntity<StoreProductResponse> createStoreProduct(@Valid @RequestBody StoreProductRequest request) {
        StoreProductResponse response = storeProductService.createStoreProduct(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Get a store product by ID (base or with offer)",
            description = "Retrieves a specific store product by its unique identifier. The product may or may not have an active offer. This endpoint returns the base product details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved store product",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreProductResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role"),
            @ApiResponse(responseCode = "404", description = "Store product not found")
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'PROFECO_ADMIN', 'STORE_ADMIN')")
    public ResponseEntity<StoreProductResponse> getStoreProductById(
            @Parameter(description = "ID of the store product to retrieve") @PathVariable UUID id) {
        return ResponseEntity.ok(storeProductService.getStoreProductById(id));
    }

    @Operation(summary = "Update an existing store product (base price/details)",
            description = "Updates base details (like price, store, or product association) of an existing store product. Offer details are managed separately.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Store product updated successfully",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreProductResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have STORE_ADMIN role"),
            @ApiResponse(responseCode = "404", description = "Store product not found")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('STORE_ADMIN')")
    public ResponseEntity<StoreProductResponse> updateStoreProduct(
            @Parameter(description = "ID of the store product to update") @PathVariable UUID id,
            @Valid @RequestBody StoreProductRequest request) {
        return ResponseEntity.ok(storeProductService.updateStoreProduct(id, request));
    }

    @Operation(summary = "Delete a store product",
            description = "Removes a store product listing. This will also remove any associated offers implicitly if cascading is set up, or may fail if offers depend on it without cascading.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Store product deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have STORE_ADMIN role"),
            @ApiResponse(responseCode = "404", description = "Store product not found")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('STORE_ADMIN')")
    public ResponseEntity<Void> deleteStoreProduct(
            @Parameter(description = "ID of the store product to delete") @PathVariable UUID id) {
        storeProductService.deleteStoreProduct(id);
        return ResponseEntity.noContent().build();
    }




    @Operation(summary = "Find store products WITHOUT offers by store ID",
            description = "Retrieves all products associated with a specific store ID that do NOT currently have an offer price set.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products without offers for the store"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role")
    })
    @GetMapping("/by-store/{storeId}/without-offer")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'PROFECO_ADMIN', 'STORE_ADMIN')")
    public ResponseEntity<List<StoreProductResponse>> getProductsByStoreIdWithoutOffer(
            @Parameter(description = "ID of the store") @PathVariable UUID storeId) {
        return ResponseEntity.ok(storeProductService.getProductsByStoreId(storeId));
    }

    @Operation(summary = "Find store products WITHOUT offers by store name",
            description = "Retrieves products by the name of the store that do NOT currently have an offer price set.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products without offers"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role")
    })
    @GetMapping("/by-store-name/without-offer")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'PROFECO_ADMIN', 'STORE_ADMIN')")
    public ResponseEntity<List<StoreProductResponse>> getProductsByStoreNameWithoutOffer(
            @Parameter(description = "Name of the store") @RequestParam String name) {
        return ResponseEntity.ok(storeProductService.getProductsByStoreName(name));
    }

    @Operation(summary = "Find store products WITHOUT offers by product name",
            description = "Retrieves products by their name across all stores that do NOT currently have an offer price set.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products without offers"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role")
    })
    @GetMapping("/by-product-name/without-offer")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'PROFECO_ADMIN', 'STORE_ADMIN')")
    public ResponseEntity<List<StoreProductResponse>> getProductsByProductNameWithoutOffer(
            @Parameter(description = "Name of the product") @RequestParam String name) {
        return ResponseEntity.ok(storeProductService.getProductsByProductName(name));
    }

    @Operation(summary = "Find store products WITHOUT offers by base price range",
            description = "Retrieves products (without offers) whose base price falls within a specified range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products without offers"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role")
    })
    @GetMapping("/by-price-range/without-offer")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'PROFECO_ADMIN', 'STORE_ADMIN')")
    public ResponseEntity<List<StoreProductResponse>> getProductsByPriceRangeWithoutOffer(
            @Parameter(description = "Minimum base price") @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum base price") @RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(storeProductService.getProductsByPriceBetween(minPrice, maxPrice));
    }

    @Operation(summary = "Find a specific store product WITHOUT an offer by store and product IDs",
            description = "Retrieves a specific product for a specific store, only if it does NOT currently have an offer price set.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved store product without offer",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = StoreProductResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role"),
            @ApiResponse(responseCode = "404", description = "Store product without offer not found for the given IDs")
    })
    @GetMapping("/by-store/{storeId}/product/{productId}/without-offer")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'PROFECO_ADMIN', 'STORE_ADMIN')")
    public ResponseEntity<StoreProductResponse> getProductByStoreAndProductIdsWithoutOffer(
            @Parameter(description = "ID of the store") @PathVariable UUID storeId,
            @Parameter(description = "ID of the product") @PathVariable UUID productId) {
        return ResponseEntity.ok(storeProductService.getProductByStoreAndProductIds(storeId, productId));
    }

    @Operation(summary = "Find store products WITHOUT offers for a specific store, ordered by price",
            description = "Retrieves products for a specific store that do NOT have an offer, ordered by their base price. This was previously 'with-valid-prices'.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved products without offers"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role")
    })
    @GetMapping("/by-store/{storeId}/without-offer/ordered-by-price")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'PROFECO_ADMIN', 'STORE_ADMIN')")
    public ResponseEntity<List<StoreProductResponse>> findStoreProductsWithoutOfferOrderedByPrice(
            @Parameter(description = "ID of the store") @PathVariable UUID storeId) {
        return ResponseEntity.ok(storeProductService.findStoreProductsWithValidPrices(storeId));
    }
}