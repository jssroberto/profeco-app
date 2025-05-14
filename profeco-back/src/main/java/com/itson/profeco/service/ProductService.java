package com.itson.profeco.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.request.ProductRequest;
import com.itson.profeco.api.dto.response.ProductResponse;
import com.itson.profeco.mapper.ProductMapper;
import com.itson.profeco.model.Product;
import com.itson.profeco.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductCategoryService productCategoryService;
    private final ProductBrandService productBrandService;

    @Transactional(readOnly = true)
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream().map(productMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(UUID id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        return productMapper.toResponse(product);
    }

    @Transactional
    public ProductResponse saveProduct(ProductRequest productRequest, String imageUrl) {
        productCategoryService.getProductCategoryById(productRequest.getCategoryId());
        productBrandService.getProductBrandById(productRequest.getBrandId());
        Product product = productMapper.toEntity(productRequest);
        product.setImageUrl(imageUrl);
        Product savedProduct = productRepository.save(product);
        return productMapper.toResponse(savedProduct);
    }

    @Transactional
    public ProductResponse updateProduct(UUID id, ProductRequest productRequest) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));

        productCategoryService.getProductCategoryById(productRequest.getCategoryId());
        productBrandService.getProductBrandById(productRequest.getBrandId());

        existingProduct.setName(productRequest.getName());
        Product productToUpdate = productMapper.toEntity(productRequest);
        productToUpdate.setId(id);
        productToUpdate.setImageUrl(existingProduct.getImageUrl());

        Product updatedProduct = productRepository.save(productToUpdate);
        return productMapper.toResponse(updatedProduct);
    }

    @Transactional
    public ProductResponse updateProductImage(UUID id, String imageUrl) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + id));
        product.setImageUrl(imageUrl);
        Product updatedProduct = productRepository.save(product);
        return productMapper.toResponse(updatedProduct);
    }

    @Transactional
    public void deleteProduct(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new EntityNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

}
