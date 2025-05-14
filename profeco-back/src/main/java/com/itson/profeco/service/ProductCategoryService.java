package com.itson.profeco.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.request.ProductCategoryRequest;
import com.itson.profeco.api.dto.response.ProductCategoryResponse;
import com.itson.profeco.mapper.ProductCategoryMapper;
import com.itson.profeco.model.ProductCategory;
import com.itson.profeco.repository.ProductCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;
    private final ProductCategoryMapper productCategoryMapper;

    @Transactional(readOnly = true)
    public List<ProductCategoryResponse> getAllProductCategories() {
        List<ProductCategory> categories = productCategoryRepository.findAll();
        return categories.stream().map(productCategoryMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductCategoryResponse getProductCategoryById(UUID id) {
        ProductCategory category = productCategoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("ProductCategory not found with id: " + id));
        return productCategoryMapper.toResponse(category);
    }

    @Transactional
    public ProductCategoryResponse saveProductCategory(
            ProductCategoryRequest productCategoryRequest) {
        productCategoryRepository.findByName(productCategoryRequest.getName())
                .ifPresent(existingCategory -> {
                    throw new IllegalArgumentException("ProductCategory with name '"
                            + productCategoryRequest.getName() + "' already exists.");
                });
        ProductCategory category = productCategoryMapper.toEntity(productCategoryRequest);
        ProductCategory savedCategory = productCategoryRepository.save(category);
        return productCategoryMapper.toResponse(savedCategory);
    }

    @Transactional
    public ProductCategoryResponse updateProductCategory(UUID id,
            ProductCategoryRequest productCategoryRequest) {
        ProductCategory existingCategory = productCategoryRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("ProductCategory not found with id: " + id));

        productCategoryRepository.findByName(productCategoryRequest.getName())
                .ifPresent(categoryWithNewName -> {
                    if (!categoryWithNewName.getId().equals(id)) {
                        throw new IllegalArgumentException("ProductCategory with name '"
                                + productCategoryRequest.getName() + "' already exists.");
                    }
                });

        existingCategory.setName(productCategoryRequest.getName());
        ProductCategory updatedCategory = productCategoryRepository.save(existingCategory);
        return productCategoryMapper.toResponse(updatedCategory);
    }

    @Transactional
    public void deleteProductCategory(UUID id) {
        if (!productCategoryRepository.existsById(id)) {
            throw new EntityNotFoundException("ProductCategory not found with id: " + id);
        }
        productCategoryRepository.deleteById(id);
    }
}
