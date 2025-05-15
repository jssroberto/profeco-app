package com.itson.profeco.controller;

import com.itson.profeco.api.dto.request.RatingRequest;
import com.itson.profeco.api.dto.response.RatingResponse;
import com.itson.profeco.service.CustomerService;
import com.itson.profeco.service.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/ratings")
@RequiredArgsConstructor
@Tag(name = "Rating Management", description = "Operations related to Store Ratings")
public class RatingController {

    private final RatingService ratingService;
    private final CustomerService contextService;

    private UUID getCurrentCustomerId() {
        return contextService.getCurrentCustomer().getId();
    }

    @Operation(summary = "Create a new rating for a store")
    @PreAuthorize("hasRole(@environment.getProperty('role.customer'))")
    @PostMapping
    public ResponseEntity<RatingResponse> createRating(@Valid @RequestBody RatingRequest ratingRequest) {
        UUID customerId = getCurrentCustomerId();
        RatingResponse createdRating = ratingService.createRating(ratingRequest, customerId);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdRating.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdRating);
    }

    @Operation(summary = "Get all ratings for a specific store")
    @GetMapping("/store/{storeId}")
    public ResponseEntity<List<RatingResponse>> getRatingsForStore(
            @Parameter(description = "ID of the store to get ratings for") @PathVariable UUID storeId) {
        List<RatingResponse> ratings = ratingService.getRatingsForStore(storeId);
        return ResponseEntity.ok(ratings);
    }

    @Operation(summary = "Get all ratings submitted by the current customer")
    @PreAuthorize("hasRole(@environment.getProperty('role.customer'))")
    @GetMapping("/my-ratings")
    public ResponseEntity<List<RatingResponse>> getMyRatings() {
        UUID customerId = getCurrentCustomerId();
        List<RatingResponse> ratings = ratingService.getRatingsByCustomer(customerId);
        return ResponseEntity.ok(ratings);
    }

    @Operation(summary = "Get a specific rating by its ID")
    @GetMapping("/{ratingId}")
    public ResponseEntity<RatingResponse> getRatingById(
            @Parameter(description = "ID of the rating to retrieve") @PathVariable UUID ratingId) {
        RatingResponse rating = ratingService.getRatingById(ratingId);
        return ResponseEntity.ok(rating);
    }

    @Operation(summary = "Get the average rating score for a specific store")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved average score or indicates no ratings",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = RatingResponse.class)))
    @ApiResponse(responseCode = "404", description = "Store not found")
    @GetMapping("/store/{storeId}/average-score")
    public ResponseEntity<Double> getAverageStoreRating(
            @Parameter(description = "ID of the store to get the average rating for") @PathVariable UUID storeId) {
        Optional<Double> averageScoreOpt = ratingService.getAverageRatingScoreForStore(storeId);
        if (averageScoreOpt.isPresent()) {
            double roundedScore = Math.round(averageScoreOpt.get() * 100.0) / 100.0;
            return ResponseEntity.ok(roundedScore);
        } else {
            return ResponseEntity.ok(0.0);
        }
    }

}