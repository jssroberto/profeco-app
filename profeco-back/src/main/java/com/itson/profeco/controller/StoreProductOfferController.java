package com.itson.profeco.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.api.dto.request.StoreOfferRequest;
import com.itson.profeco.api.dto.response.StoreOfferResponse;
import com.itson.profeco.api.dto.response.StoreProductResponse;
import com.itson.profeco.service.StoreProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/store-product-offers")
@RequiredArgsConstructor
@Tag(name = "Store Product Offer Management",
        description = "APIs for managing offers on store products")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasAnyRole(@environment.getProperty('role.customer'), @environment.getProperty('role.store-admin'), @environment.getProperty('role.profeco-admin'))")
public class StoreProductOfferController {

    private final StoreProductService storeProductService;

    @Operation(summary = "Get the current offer for a store product",
            description = "Retrieves the active offer details for a specific store product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved offer details",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StoreOfferResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Store product or offer not found")})
    @GetMapping("/{storeProductId}")
    public ResponseEntity<StoreOfferResponse> getOfferForStoreProduct(
            @Parameter(description = "ID of the store product") @PathVariable UUID storeProductId) {
        return ResponseEntity.ok(storeProductService.getOfferForStoreProduct(storeProductId));
    }

    @Operation(summary = "Get all store products that currently have any offer",
            description = "Retrieves a list of all store products where an offer price is set.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved products with offers"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @GetMapping("/active-any")
    public ResponseEntity<List<StoreOfferResponse>> getProductsWithAnyOffer() {
        return ResponseEntity.ok(storeProductService.getProductsWithAnyOffer());
    }

    @Operation(summary = "Get offers active within a specific date range",
            description = "Retrieves store products with offers that are active between the given start and end dates (inclusive).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved active offers"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @GetMapping("/active-between-dates")
    public ResponseEntity<List<StoreOfferResponse>> getOffersActiveBetweenDates(
            @Parameter(description = "Start date (YYYY-MM-DD)") @RequestParam @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (YYYY-MM-DD)") @RequestParam @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity
                .ok(storeProductService.getOffersActiveBetweenDates(startDate, endDate));
    }

    @Operation(summary = "Get offers by offer price range",
            description = "Retrieves store products with offers where the offer price falls within the specified range.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved offers by price range"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @GetMapping("/by-offer-price-range")
    public ResponseEntity<List<StoreOfferResponse>> getOffersByOfferPriceRange(
            @Parameter(description = "Minimum offer price") @RequestParam BigDecimal minPrice,
            @Parameter(description = "Maximum offer price") @RequestParam BigDecimal maxPrice) {
        return ResponseEntity.ok(storeProductService.getOffersByPriceRange(minPrice, maxPrice));
    }

    @Operation(summary = "Find active offers filtered by optional store and product IDs",
            description = "Retrieves store products that have an active offer, optionally filtered by store ID and/or product ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved active offers"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @GetMapping("/active-filtered")
    public ResponseEntity<List<StoreOfferResponse>> findActiveOffersFiltered(
            @Parameter(description = "Optional ID of the store") @RequestParam(
                    required = false) UUID storeId,
            @Parameter(description = "Optional ID of the product") @RequestParam(
                    required = false) UUID productId) {
        return ResponseEntity.ok(storeProductService.findActiveOffersFiltered(storeId, productId));
    }

    @Operation(summary = "Find current offers for a specific product on a given date",
            description = "Retrieves offers for a product that are active on the specified current date.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Successfully retrieved current product offers"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden")})
    @GetMapping("/current-for-product/{productId}")
    public ResponseEntity<List<StoreOfferResponse>> findCurrentProductOffers(
            @Parameter(description = "ID of the product") @PathVariable UUID productId,
            @Parameter(
                    description = "Current date to check offers against (YYYY-MM-DD)") @RequestParam @DateTimeFormat(
                            iso = DateTimeFormat.ISO.DATE) LocalDate currentDate) {
        return ResponseEntity
                .ok(storeProductService.findCurrentProductOffers(productId, currentDate));
    }

    @Operation(summary = "Apply or update an offer for a store product",
            description = "Applies a new offer or updates an existing one for a specific store product. The request body should contain offer details including the product ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer applied/updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = StoreOfferResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request body or offer dates"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - User does not have required permissions"),
            @ApiResponse(responseCode = "404",
                    description = "Store product not found for the given product ID in the current store")})
    @PostMapping("/apply") // Removed storeProductId from path
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole(@environment.getProperty('role.store-admin'))")
    public ResponseEntity<StoreOfferResponse> saveOffer(
            // Removed @PathVariable UUID storeProductId
            @Parameter(
                    description = "Offer details including product ID, offer price, start date, and end date.") @Valid @RequestBody StoreOfferRequest request) {
        // The productId is now part of the request object
        StoreOfferResponse response = storeProductService.applyOrUpdateOffer(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove an offer from a store product",
            description = "Clears the offer details (price, dates, inconsistency) from a specific store product.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200",
            description = "Offer removed successfully, returning the product state without offer",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = StoreOfferResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - User does not have required permissions"),
            @ApiResponse(responseCode = "404", description = "Store product not found")})
    @DeleteMapping("/{storeProductId}/remove")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole(@environment.getProperty('role.store-admin'))")
    public ResponseEntity<StoreOfferResponse> removeOffer(@Parameter(
            description = "ID of the store product from which to remove the offer") @PathVariable UUID storeProductId) {
        return ResponseEntity.ok(storeProductService.removeOffer(storeProductId));
    }

    @Operation(summary = "Find all products by store ID",
            description = "Retrieves all products (with or without offers) for a specific store.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200",
                            description = "Successfully retrieved all products for the store",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(
                                            implementation = StoreProductResponse.class)))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden - User does not have required role"),
                    @ApiResponse(responseCode = "404",
                            description = "Store not found (if service layer adds this check)")})
    @GetMapping("/store/{storeId}/all-products") // Nueva ruta
    public ResponseEntity<List<StoreOfferResponse>> getAllStoreProductsByStoreId(@Parameter(
            description = "ID of the store to retrieve all products from") @PathVariable UUID storeId) {
        List<StoreOfferResponse> products =
                storeProductService.getAllStoreProductsByStoreId(storeId);
        return ResponseEntity.ok(products);
    }

}
