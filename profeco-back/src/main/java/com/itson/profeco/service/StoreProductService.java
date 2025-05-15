package com.itson.profeco.service;

import com.itson.profeco.api.dto.request.StoreOfferRequest;
import com.itson.profeco.api.dto.request.StoreProductRequest;
import com.itson.profeco.api.dto.response.StoreOfferResponse;
import com.itson.profeco.api.dto.response.StoreProductResponse;
import com.itson.profeco.exceptions.ResourceNotFoundException;
import com.itson.profeco.mapper.StoreProductMapper;
import com.itson.profeco.model.Inconsistency;
import com.itson.profeco.model.Product;
import com.itson.profeco.model.Store;
import com.itson.profeco.model.StoreProduct;
import com.itson.profeco.repository.InconsistencyRepository;
import com.itson.profeco.repository.ProductRepository;
import com.itson.profeco.repository.StoreProductRepository;
import com.itson.profeco.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class StoreProductService {

    private final StoreProductRepository storeProductRepository;
    private final StoreProductMapper storeProductMapper;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    @Transactional
    public StoreProductResponse createStoreProduct(StoreProductRequest request) {
        Store store = storeRepository.findById(request.getStore())
                .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + request.getStore()));

        Product product = productRepository.findById(request.getProduct())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + request.getProduct()));


        StoreProduct storeProduct = storeProductMapper.productRequestToEntity(request);

        storeProduct.setStore(store);
        storeProduct.setProduct(product);
        storeProduct.setOfferPrice(null);
        storeProduct.setOfferStartDate(null);
        storeProduct.setOfferEndDate(null);
        storeProduct.setInconsistency(null);

        StoreProduct savedProduct = storeProductRepository.save(storeProduct);
        return storeProductMapper.entityToProductResponse(savedProduct);
    }

    @Transactional(readOnly = true)
    public StoreProductResponse getStoreProductById(UUID id) {
        StoreProduct storeProduct = findStoreProductEntityById(id);
        return storeProductMapper.entityToProductResponse(storeProduct);
    }

    @Transactional
    public StoreProductResponse updateStoreProduct(UUID id, StoreProductRequest request) {
        StoreProduct existingProduct = findStoreProductEntityById(id);

        if (request.getStore() != null) {
            Store store = storeRepository.findById(request.getStore())
                    .orElseThrow(() -> new ResourceNotFoundException("Store not found with ID: " + request.getStore()));
            existingProduct.setStore(store);
        }

        if (request.getProduct() != null) {
            Product product = productRepository.findById(request.getProduct())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + request.getProduct()));
            existingProduct.setProduct(product);
        }

        storeProductMapper.updateEntityFromProductRequest(existingProduct, request);

        StoreProduct updatedProduct = storeProductRepository.save(existingProduct);
        return storeProductMapper.entityToProductResponse(updatedProduct);
    }

    @Transactional
    public void deleteStoreProduct(UUID id) {
        if (!storeProductRepository.existsById(id)) {
            throw new ResourceNotFoundException("StoreProduct not found with id: " + id);
        }
        storeProductRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public StoreOfferResponse getOfferForStoreProduct(UUID storeProductId) {
        StoreProduct storeProduct = findStoreProductEntityById(storeProductId);
        if (storeProduct.getOfferPrice() == null && storeProduct.getOfferStartDate() == null) {
            throw new ResourceNotFoundException("No offer details found for StoreProduct id: " + storeProductId);
        }
        return storeProductMapper.entityToOfferResponse(storeProduct);
    }

    @Transactional
    public StoreOfferResponse applyOrUpdateOffer(UUID storeProductId, StoreOfferRequest request) {
        validateOfferDates(request.getOfferStartDate(), request.getOfferEndDate());
        StoreProduct existingProduct = findStoreProductEntityById(storeProductId);

        if (request.getOfferPrice() != null &&
                (existingProduct.getOfferPrice() == null || !existingProduct.getOfferPrice().equals(request.getOfferPrice())) &&
                storeProductRepository.existsByProductAndStoreAndOfferPrice(
                        existingProduct.getProduct(),
                        existingProduct.getStore(),
                        request.getOfferPrice())) {
            throw new DataIntegrityViolationException(
                    "An offer already exists for this product in this store with the same target offer price");
        }
        storeProductMapper.updateOfferFieldsInStoreProduct(existingProduct, request);
        StoreProduct updatedProduct = storeProductRepository.save(existingProduct);
        return storeProductMapper.entityToOfferResponse(updatedProduct);
    }
    @Transactional
    public StoreOfferResponse removeOffer(UUID storeProductId) {
        StoreProduct existingProduct = findStoreProductEntityById(storeProductId);
        existingProduct.setOfferPrice(null);
        existingProduct.setOfferStartDate(null);
        existingProduct.setOfferEndDate(null);
        existingProduct.setInconsistency(null);
        StoreProduct updatedProduct = storeProductRepository.save(existingProduct);
        return storeProductMapper.entityToOfferResponse(updatedProduct);
    }
    @Transactional(readOnly = true)
    public List<StoreOfferResponse> getProductsWithAnyOffer() {
        return storeProductRepository.findByOfferPriceNotNull().stream()
                .map(storeProductMapper::entityToOfferResponse)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<StoreOfferResponse> getOffersActiveBetweenDates(LocalDate startDate, LocalDate endDate) {
        return storeProductRepository.findByOfferStartDateLessThanEqualAndOfferEndDateGreaterThanEqual(startDate, endDate).stream()
                .map(storeProductMapper::entityToOfferResponse)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<StoreOfferResponse> getOffersByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return storeProductRepository.findByOfferPriceBetween(minPrice, maxPrice).stream()
                .map(storeProductMapper::entityToOfferResponse)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<StoreOfferResponse> findActiveOffersFiltered(UUID storeId, UUID productId) {
        return storeProductRepository.findActiveOffers(storeId, productId).stream()
                .map(storeProductMapper::entityToOfferResponse)
                .collect(Collectors.toList());
    }
    @Transactional(readOnly = true)
    public List<StoreOfferResponse> findCurrentProductOffers(UUID productId, LocalDate currentDate) {
        return storeProductRepository.findCurrentProductOffers(productId, currentDate).stream()
                .map(storeProductMapper::entityToOfferResponse)
                .collect(Collectors.toList());
    }



    @Transactional(readOnly = true)
    public List<StoreProductResponse> getProductsByStoreId(UUID storeId) {
        return storeProductRepository.findByStore_IdAndOfferPriceIsNull(storeId).stream()
                .map(storeProductMapper::entityToProductResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreProductResponse> getProductsByStoreName(String storeName) {
        return storeProductRepository.findByStore_NameAndOfferPriceIsNull(storeName).stream()
                .map(storeProductMapper::entityToProductResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreProductResponse> getProductsByProductName(String productName) {
        return storeProductRepository.findByProduct_NameAndOfferPriceIsNull(productName).stream()
                .map(storeProductMapper::entityToProductResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreProductResponse> getProductsByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return storeProductRepository.findByPriceBetweenAndOfferPriceIsNull(minPrice, maxPrice).stream()
                .map(storeProductMapper::entityToProductResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StoreProductResponse getProductByStoreAndProductIds(UUID storeId, UUID productId) {
        return storeProductRepository.findOneByStore_IdAndProduct_IdAndOfferPriceIsNull(storeId, productId)
                .map(storeProductMapper::entityToProductResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "StoreProduct without offer not found for Store ID: " + storeId + " and Product ID: " + productId));
    }

    @Transactional(readOnly = true)
    public List<StoreProductResponse> findStoreProductsWithValidPrices(UUID storeId) {
        return storeProductRepository.findStoreProductsWithoutOfferOrderedByPrice(storeId).stream()
                .map(storeProductMapper::entityToProductResponse)
                .collect(Collectors.toList());
    }

    private StoreProduct findStoreProductEntityById(UUID id) {
        return storeProductRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StoreProduct not found with id: " + id));
    }

    private void validateOfferDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Offer start date and end date cannot be null.");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Offer start date must be before or equal to end date.");
        }
    }


}