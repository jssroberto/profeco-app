package com.itson.profeco.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.api.dto.request.StoreProductRequest;
import com.itson.profeco.api.dto.response.StoreProductResponse;
import com.itson.profeco.service.StoreProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/store-products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole(@environment.getProperty('role.customer'), @environment.getProperty('role.store-admin'), @environment.getProperty('role.profeco-admin'))")
@Tag(name = "Store Product Management (Base Products)",
        description = "APIs for managing base products (without offers) within stores.")
public class StoreProductController {

    private final StoreProductService storeProductService;

    @Operation(summary = "Get all store products",
            description = "Retrieves all store products. Accessible only by customers.")
    @GetMapping
    @PreAuthorize("hasRole(@environment.getProperty('role.customer'))")
    public ResponseEntity<List<StoreProductResponse>> getAllStoreProducts() {
        return ResponseEntity.ok(storeProductService.getAllStoreProducts());
    }

    @Operation(summary = "Get store product by ID",
            description = "Retrieves a store product by its ID. Returns base product details.")
    @GetMapping("/{id}")
    public ResponseEntity<StoreProductResponse> getStoreProductById(
            @Parameter(description = "ID of the store product to retrieve") @PathVariable UUID id) {
        return ResponseEntity.ok(storeProductService.getStoreProductById(id));
    }

    @Operation(summary = "Find store products by store ID independent of offers",
            description = "Retrieves products for a store that don't have an offer price.")
    @GetMapping("/by-store/{storeId}")
    public ResponseEntity<List<StoreProductResponse>> getProductsByStoreIdWithoutOffer(
            @Parameter(description = "ID of the store") @PathVariable UUID storeId) {
        return ResponseEntity.ok(storeProductService.getProductsByStoreId(storeId));
    }

    @Operation(summary = "Find store products without offers by store name",
            description = "Retrieves products by store name that don't have an offer price.")
    @GetMapping("/by-store-name")
    public ResponseEntity<List<StoreProductResponse>> getProductsByStoreNameWithoutOffer(
            @Parameter(description = "Name of the store") @RequestParam String name) {
        return ResponseEntity.ok(storeProductService.getProductsByStoreName(name));
    }

    @Operation(summary = "Find store products without offers by product name",
            description = "Retrieves products by product name across all stores that don't have an offer price.")
    @GetMapping("/by-product-name")
    public ResponseEntity<List<StoreProductResponse>> getProductsByProductNameWithoutOffer(
            @Parameter(description = "Name of the product") @RequestParam String name) {
        return ResponseEntity.ok(storeProductService.getProductsByProductName(name));
    }

    @Operation(summary = "Find store products without offers by base price range",
            description = "Retrieves products without offers within a specified base price range.")
    @GetMapping("/by-price-range")
    public ResponseEntity<List<StoreProductResponse>> getProductsByPriceRangeWithoutOffer(
            @Parameter(description = "Minimum base price") @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum base price") @RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(storeProductService.getProductsByPriceBetween(minPrice, maxPrice));
    }

    @Operation(summary = "Find specific store product without offer by store and product IDs",
            description = "Retrieves a specific product for a store, if it doesn't have an offer price.")
    @GetMapping("/by-store/{storeId}/product/{productId}")
    public ResponseEntity<StoreProductResponse> getProductByStoreAndProductIdsWithoutOffer(
            @Parameter(description = "ID of the store") @PathVariable UUID storeId,
            @Parameter(description = "ID of the product") @PathVariable UUID productId) {
        return ResponseEntity
                .ok(storeProductService.getProductByStoreAndProductIds(storeId, productId));
    }

    @Operation(summary = "Find store products without offers by store, ordered by price",
            description = "Retrieves products for a store (no offers) ordered by base price.")
    @GetMapping("/by-store/{storeId}/ordered-by-price")
    public ResponseEntity<List<StoreProductResponse>> findStoreProductsWithoutOfferOrderedByPrice(
            @Parameter(description = "ID of the store") @PathVariable UUID storeId) {
        return ResponseEntity.ok(storeProductService.findStoreProductsWithValidPrices(storeId));
    }

    @Operation(summary = "Create store product",
            description = "Adds a new product to a store with its base price.")
    @PostMapping
    @PreAuthorize("hasRole(@environment.getProperty('role.store-admin'))")
    public ResponseEntity<StoreProductResponse> createStoreProduct(
            @Valid @RequestBody StoreProductRequest request) {
        StoreProductResponse response = storeProductService.createStoreProduct(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Update store product by Product ID in current store",
            description = "Updates a store product's price and optional offer details.")
    @PutMapping("/by-product")
    @PreAuthorize("hasRole(@environment.getProperty('role.store-admin'))")
    public ResponseEntity<StoreProductResponse> updateStoreProductByProductInCurrentStore(
            @Valid @RequestBody StoreProductRequest request) {
        return ResponseEntity
                .ok(storeProductService.updateStoreProductByProductInCurrentStore(request));
    }

    @Operation(summary = "Delete store product", description = "Removes a store product listing.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole(@environment.getProperty('role.store-admin'))")
    public ResponseEntity<Void> deleteStoreProduct(
            @Parameter(description = "ID of the store product to delete") @PathVariable UUID id) {
        storeProductService.deleteStoreProduct(id);
        return ResponseEntity.noContent().build();
    }



}
