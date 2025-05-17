package com.itson.profeco.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.request.StoreOfferRequest;
import com.itson.profeco.api.dto.request.StoreProductRequest;
import com.itson.profeco.api.dto.response.NewOfferEventPayload;
import com.itson.profeco.api.dto.response.StoreOfferResponse;
import com.itson.profeco.api.dto.response.StoreProductResponse;
import com.itson.profeco.config.RabbitConfig;
import com.itson.profeco.exceptions.OperationNotAllowedException;
import com.itson.profeco.exceptions.ResourceNotFoundException;
import com.itson.profeco.mapper.StoreProductMapper;
import com.itson.profeco.model.Product;
import com.itson.profeco.model.Store;
import com.itson.profeco.model.StoreProduct;
import com.itson.profeco.repository.ProductRepository;
import com.itson.profeco.repository.StoreProductRepository;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class StoreProductService {

    private final StoreProductRepository storeProductRepository;
    private final StoreProductMapper storeProductMapper;
    private final ProductRepository productRepository;
    private final StoreAdminService storeAdminService;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public StoreProductResponse createStoreProduct(StoreProductRequest request) {
        Store store = storeAdminService.getAuthenticatedStoreAdminStore();

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + request.getProductId()));
        List<StoreProduct> existingEntries =
                storeProductRepository.findByStore_IdAndProduct_Id(store.getId(), product.getId());
        if (!existingEntries.isEmpty()) {
            throw new DataIntegrityViolationException("Product already exists in store.");
        }

        StoreProduct storeProduct = storeProductMapper.productRequestToEntity(request);
        storeProduct.setStore(store);
        storeProduct.setProduct(product);
        storeProduct.setOfferPrice(null);
        storeProduct.setOfferStartDate(null);
        storeProduct.setOfferEndDate(null);

        StoreProduct savedProduct = storeProductRepository.save(storeProduct);
        return storeProductMapper.entityToProductResponse(savedProduct);
    }

    @Transactional
    public StoreProductResponse updateStoreProductByProductInCurrentStore(
            StoreProductRequest request) {
        Store currentUserStore = storeAdminService.getAuthenticatedStoreAdminStore();

        if (request.getProductId() == null) {
            throw new IllegalArgumentException(
                    "Product ID must be provided in the request to identify the store product to update.");
        }
        UUID targetProductId = request.getProductId();

        StoreProduct existingProduct = storeProductRepository
                .findByStore_IdAndProduct_Id(currentUserStore.getId(), targetProductId).stream()
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "StoreProduct not found for Product ID: " + targetProductId
                                + " in the current admin's store (ID: " + currentUserStore.getId()
                                + "). Cannot update."));


        if (request.getPrice() != null) {
            existingProduct.setPrice(request.getPrice());
        }

        if (existingProduct.getOfferPrice() != null && existingProduct.getPrice()
                .floatValue() < existingProduct.getOfferPrice().floatValue()) {
            existingProduct.setOfferPrice(null);
            existingProduct.setOfferStartDate(null);
            existingProduct.setOfferEndDate(null);
        }


        StoreProduct updatedProduct = storeProductRepository.save(existingProduct);
        return storeProductMapper.entityToProductResponse(updatedProduct);
    }

    @Transactional(readOnly = true)
    public StoreProductResponse getStoreProductById(UUID id) {
        StoreProduct storeProduct = findStoreProductEntityById(id);
        return storeProductMapper.entityToProductResponse(storeProduct);
    }



    @Transactional
    public void deleteStoreProduct(UUID id) {
        StoreProduct existingProduct = findStoreProductEntityById(id);
        Store currentUserStore = storeAdminService.getAuthenticatedStoreAdminStore();
        if (!existingProduct.getStore().getId().equals(currentUserStore.getId())) {
            throw new OperationNotAllowedException(
                    "STORE_ADMIN can only delete products belonging to their own store.");
        }
        storeProductRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public StoreOfferResponse getOfferForStoreProduct(UUID storeProductId) {
        StoreProduct storeProduct = findStoreProductEntityById(storeProductId);
        if (storeProduct.getOfferPrice() == null && storeProduct.getOfferStartDate() == null) {
            throw new ResourceNotFoundException(
                    "No offer details found for StoreProduct id: " + storeProductId);
        }
        return storeProductMapper.entityToOfferResponse(storeProduct);
    }
















    @Transactional
    public StoreOfferResponse applyOrUpdateOffer(StoreOfferRequest request) {
        validateOfferDates(request.getOfferStartDate(), request.getOfferEndDate());

        Store currentUserStore = storeAdminService.getAuthenticatedStoreAdminStore();
        StoreProduct existingProduct = storeProductRepository
                .findByStore_IdAndProduct_Id(currentUserStore.getId(), request.getProductId())
                .stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException(
                        "StoreProduct not found for Product ID: " + request.getProductId()
                                + " in the current admin's store (ID: " + currentUserStore.getId()
                                + "). Cannot apply offer."));

        storeProductMapper.updateOfferFieldsInStoreProduct(existingProduct, request);
        StoreProduct updatedProductWithOffer = storeProductRepository.save(existingProduct);

        if (updatedProductWithOffer.getOfferPrice() != null
                && updatedProductWithOffer.getOfferStartDate() != null
                && updatedProductWithOffer.getOfferEndDate() != null) {

            String offerDescription = String.format("¡Oferta en %s por solo $%.2f!",
                    updatedProductWithOffer.getProduct().getName(),
                    updatedProductWithOffer.getOfferPrice());

            NewOfferEventPayload offerEvent = new NewOfferEventPayload(
                    updatedProductWithOffer.getId(), updatedProductWithOffer.getStore().getId(),
                    updatedProductWithOffer.getStore().getName(),
                    updatedProductWithOffer.getProduct().getId(),
                    updatedProductWithOffer.getProduct().getName(),
                    updatedProductWithOffer.getOfferPrice(),
                    updatedProductWithOffer.getOfferStartDate(),
                    updatedProductWithOffer.getOfferEndDate(), offerDescription);

            rabbitTemplate.convertAndSend(RabbitConfig.EXCHANGE_NEW_OFFERS_FANOUT, // El exchange
                                                                                   // fanout
                    "", offerEvent);

        }
        return storeProductMapper.entityToOfferResponse(updatedProductWithOffer);
    }














    @Transactional
    public StoreOfferResponse removeOffer(UUID storeProductId) {
        StoreProduct existingProduct = findStoreProductEntityById(storeProductId);
        Store currentUserStore = storeAdminService.getAuthenticatedStoreAdminStore();
        if (!existingProduct.getStore().getId().equals(currentUserStore.getId())) {
            throw new OperationNotAllowedException(
                    "STORE_ADMIN can only remove offers for products in their own store.");
        }
        existingProduct.setOfferPrice(null);
        existingProduct.setOfferStartDate(null);
        existingProduct.setOfferEndDate(null);
        StoreProduct updatedProduct = storeProductRepository.save(existingProduct);
        return storeProductMapper.entityToOfferResponse(updatedProduct);
    }

    @Transactional(readOnly = true)
    public List<StoreOfferResponse> getProductsWithAnyOffer() {
        return storeProductRepository.findAllByOfferPriceNotNullAndOfferDateActive(LocalDate.now())
                .stream().map(storeProductMapper::entityToOfferResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreOfferResponse> getOffersActiveBetweenDates(LocalDate startDate,
            LocalDate endDate) {
        return storeProductRepository
                .findByOfferStartDateLessThanEqualAndOfferEndDateGreaterThanEqual(startDate,
                        endDate)
                .stream().map(storeProductMapper::entityToOfferResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreOfferResponse> getOffersByPriceRange(BigDecimal minPrice,
            BigDecimal maxPrice) {
        return storeProductRepository.findByOfferPriceBetween(minPrice, maxPrice).stream()
                .map(storeProductMapper::entityToOfferResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreOfferResponse> findActiveOffersFiltered(UUID storeId, UUID productId) {
        return storeProductRepository.findActiveOffers(storeId, productId).stream()
                .map(storeProductMapper::entityToOfferResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreOfferResponse> findCurrentProductOffers(UUID productId,
            LocalDate currentDate) {
        return storeProductRepository.findCurrentProductOffers(productId, currentDate).stream()
                .map(storeProductMapper::entityToOfferResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreProductResponse> getProductsByStoreId(UUID storeId) {
        return storeProductRepository.findByStore_Id(storeId).stream()
                .map(storeProductMapper::entityToProductResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreProductResponse> getProductsByStoreName(String storeName) {
        return storeProductRepository.findByStore_NameAndOfferPriceIsNull(storeName).stream()
                .map(storeProductMapper::entityToProductResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreProductResponse> getProductsByProductName(String productName) {
        return storeProductRepository.findByProduct_NameAndOfferPriceIsNull(productName).stream()
                .map(storeProductMapper::entityToProductResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreProductResponse> getProductsByPriceBetween(BigDecimal minPrice,
            BigDecimal maxPrice) {
        return storeProductRepository.findByPriceBetweenAndOfferPriceIsNull(minPrice, maxPrice)
                .stream().map(storeProductMapper::entityToProductResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StoreProductResponse getProductByStoreAndProductIds(UUID storeId, UUID productId) {
        return storeProductRepository
                .findOneByStore_IdAndProduct_IdAndOfferPriceIsNull(storeId, productId)
                .map(storeProductMapper::entityToProductResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "StoreProduct without offer not found for Store ID: " + storeId
                                + " and Product ID: " + productId));
    }

    @Transactional(readOnly = true)
    public List<StoreProductResponse> findStoreProductsWithValidPrices(UUID storeId) {
        return storeProductRepository.findStoreProductsWithoutOfferOrderedByPrice(storeId).stream()
                .map(storeProductMapper::entityToProductResponse).collect(Collectors.toList());
    }

    private StoreProduct findStoreProductEntityById(UUID id) {
        return storeProductRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("StoreProduct not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<StoreProductResponse> getAllStoreProducts() {
        return storeProductRepository.findAll().stream()
                .map(storeProductMapper::entityToProductResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<StoreOfferResponse> getAllStoreProductsByStoreId(UUID storeId) {
        List<StoreProduct> storeProducts =
                storeProductRepository.findByStore_IdAndOfferPriceIsNotNull(storeId);
        return storeProducts.stream().map(storeProductMapper::entityToOfferResponse)
                .collect(Collectors.toList());
    }



    private void validateOfferDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Offer start date and end date cannot be null.");
        }
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "Offer start date must be before or equal to end date.");
        }
    }
}
