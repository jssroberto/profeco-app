package com.itson.profeco.controller;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.itson.profeco.api.dto.request.ProductRequest;
import com.itson.profeco.api.dto.response.ProductResponse;
import com.itson.profeco.service.FileStorageService;
import com.itson.profeco.service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.parameters.RequestBody; // Corrected import
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Operations related to Product")
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;
    private final FileStorageService fileStorageService;

    @Operation(summary = "Get all products", description = "Returns a list of all products.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of products returned successfully") })
    @GetMapping
    @PreAuthorize("hasRole(@environment.getProperty('role.store-admin')) or hasRole(@environment.getProperty('role.customer'))")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> responses = productService.getAllProducts();
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Get product by ID", description = "Returns a product by its ID.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found") })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole(@environment.getProperty('role.store-admin')) or hasRole(@environment.getProperty('role.customer')) or hasRole(@environment.getProperty('role.profeco-admin'))")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable UUID id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Create product", description = "Creates a new product.", requestBody = @RequestBody(content = @Content(encoding = {
            @Encoding(name = "productRequest", contentType = MediaType.APPLICATION_JSON_VALUE),
            @Encoding(name = "image", contentType = MediaType.MULTIPART_FORM_DATA_VALUE) })))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input") })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole(@environment.getProperty('role.store-admin'))")
    public ResponseEntity<ProductResponse> saveProduct(
            @Valid @RequestPart("productRequest") ProductRequest productRequest,
            @RequestPart("image") MultipartFile image) {
        String uniqueFilename = fileStorageService.store(image);
        String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/images/").path(uniqueFilename).toUriString();

        ProductResponse savedProduct = productService.saveProduct(productRequest, imageUrl);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedProduct.getId()).toUri();
        return ResponseEntity.created(location).body(savedProduct);
    }

}
