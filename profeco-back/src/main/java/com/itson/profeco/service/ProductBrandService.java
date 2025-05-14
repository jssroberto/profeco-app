package com.itson.profeco.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.request.ProductBrandRequest;
import com.itson.profeco.api.dto.response.ProductBrandResponse;
import com.itson.profeco.mapper.ProductBrandMapper;
import com.itson.profeco.model.ProductBrand;
import com.itson.profeco.repository.ProductBrandRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductBrandService {

    private final ProductBrandRepository productBrandRepository;
    private final ProductBrandMapper productBrandMapper;

    @Transactional(readOnly = true)
    public List<ProductBrandResponse> getAllProductBrands() {
        List<ProductBrand> productBrands = productBrandRepository.findAll();
        return productBrands.stream().map(productBrandMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductBrandResponse getProductBrandById(UUID id) {
        ProductBrand productBrand = productBrandRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("ProductBrand not found with id: " + id));
        return productBrandMapper.toResponse(productBrand);
    }

    @Transactional
    public ProductBrandResponse saveProductBrand(ProductBrandRequest productBrandRequest) {
        productBrandRepository.findByName(productBrandRequest.getName()).ifPresent(pb -> {
            throw new IllegalArgumentException("ProductBrand with name '"
                    + productBrandRequest.getName() + "' already exists.");
        });
        ProductBrand productBrand = productBrandMapper.toEntity(productBrandRequest);
        ProductBrand savedProductBrand = productBrandRepository.save(productBrand);
        return productBrandMapper.toResponse(savedProductBrand);
    }

    @Transactional
    public ProductBrandResponse updateProductBrand(UUID id,
            ProductBrandRequest productBrandRequest) {
        ProductBrand existingProductBrand = productBrandRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("ProductBrand not found with id: " + id));

        productBrandRepository.findByName(productBrandRequest.getName()).ifPresent(pb -> {
            if (!pb.getId().equals(id)) {
                throw new IllegalArgumentException("ProductBrand with name '"
                        + productBrandRequest.getName() + "' already exists.");
            }
        });

        existingProductBrand.setName(productBrandRequest.getName());
        ProductBrand updatedProductBrand = productBrandRepository.save(existingProductBrand);
        return productBrandMapper.toResponse(updatedProductBrand);
    }

    @Transactional
    public void deleteProductBrand(UUID id) {
        if (!productBrandRepository.existsById(id)) {
            throw new EntityNotFoundException("ProductBrand not found with id: " + id);
        }
        productBrandRepository.deleteById(id);
    }

}
