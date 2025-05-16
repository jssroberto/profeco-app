package com.itson.profeco.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.itson.profeco.model.Product;
import com.itson.profeco.model.Store;
import com.itson.profeco.model.StoreProduct;

public interface StoreProductRepository extends JpaRepository<StoreProduct, UUID> {

    List<StoreProduct> findByStore_IdAndOfferPriceIsNull(UUID storeId);

    List<StoreProduct> findByStore_NameAndOfferPriceIsNull(String storeName);

    List<StoreProduct> findByProduct_NameAndOfferPriceIsNull(String productName);

    List<StoreProduct> findByPriceBetweenAndOfferPriceIsNull(BigDecimal minPrice, BigDecimal maxPrice);

    @Query("SELECT sp FROM StoreProduct sp WHERE sp.store.id = :storeId AND sp.product.id = :productId AND sp.offerPrice IS NULL")
    Optional<StoreProduct> findOneByStore_IdAndProduct_IdAndOfferPriceIsNull(@Param("storeId") UUID storeId,
            @Param("productId") UUID productId);

    @Query(value = "SELECT * FROM store_product sp " +
            "WHERE sp.store_id = :storeId " +
            "AND sp.offer_price IS NULL " +
            "ORDER BY sp.price ASC", nativeQuery = true)
    List<StoreProduct> findStoreProductsWithoutOfferOrderedByPrice(@Param("storeId") UUID storeId);

    List<StoreProduct> findByOfferPriceNotNull();

    List<StoreProduct> findByOfferStartDateLessThanEqualAndOfferEndDateGreaterThanEqual(LocalDate startDate,
            LocalDate endDate);

    List<StoreProduct> findByOfferPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    @Query("SELECT sp FROM StoreProduct sp WHERE sp.offerPrice IS NOT NULL AND sp.offerStartDate <= :currentDate AND sp.offerEndDate >= :currentDate")
    List<StoreProduct> findAllByOfferPriceNotNullAndOfferDateActive(@Param("currentDate") LocalDate currentDate);

    boolean existsByProductAndStoreAndOfferPrice(Product product, Store store, BigDecimal offerPrice);

    List<StoreProduct> findByStore_IdAndProduct_Id(UUID storeId, UUID productId);

    @Query("SELECT sp FROM StoreProduct sp WHERE " +
            "sp.offerPrice IS NOT NULL AND " +
            "(:storeId IS NULL OR sp.store.id = :storeId) AND " +
            "(:productId IS NULL OR sp.product.id = :productId)")
    List<StoreProduct> findActiveOffers(@Param("storeId") UUID storeId,
            @Param("productId") UUID productId);

    @Query("SELECT sp FROM StoreProduct sp WHERE " +
            "sp.product.id = :productId AND " +
            "sp.offerStartDate <= :currentDate AND " +
            "sp.offerEndDate >= :currentDate")
    List<StoreProduct> findCurrentProductOffers(@Param("productId") UUID productId,
            @Param("currentDate") LocalDate currentDate);

}
