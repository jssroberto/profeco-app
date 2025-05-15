package com.itson.profeco.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import com.itson.profeco.model.Product;
import com.itson.profeco.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.StoreProduct;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StoreProductRepository extends JpaRepository<StoreProduct, UUID> {


    List<StoreProduct> findByOfferPriceNotNull();

    List<StoreProduct> findByOfferStartDateLessThanEqualAndOfferEndDateGreaterThanEqual(LocalDate startDate, LocalDate endDate);

    List<StoreProduct> findByOfferStartDateBeforeAndOfferEndDateAfter(LocalDate date1, LocalDate date2);

    List<StoreProduct> findByOfferPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<StoreProduct> findByStore_Id(UUID storeId);

    List<StoreProduct> findByStore_Name(String storeName);

    List<StoreProduct> findByStore_NameContainingIgnoreCase(String storeNamePart);

    List<StoreProduct> findByProduct_Id(UUID productId);

    List<StoreProduct> findByProduct_Name(String productName);

    List<StoreProduct> findByProduct_NameContainingIgnoreCase(String productNamePart);

    List<StoreProduct> findByPrice(BigDecimal price);

    List<StoreProduct> findByPriceLessThan(BigDecimal price);

    List<StoreProduct> findByPriceLessThanEqual(BigDecimal price);

    List<StoreProduct> findByPriceGreaterThan(BigDecimal price);

    List<StoreProduct> findByPriceGreaterThanEqual(BigDecimal price);

    List<StoreProduct> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<StoreProduct> findByStore_IdAndProduct_Id(UUID storeId, UUID productId);

    boolean existsByProductAndStoreAndOfferPrice(Product product, Store store, BigDecimal offerPrice);

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

    @Query(value = "SELECT * FROM store_product sp " +
            "WHERE sp.store_id = :storeId " +
            "AND (sp.offer_price IS NULL OR sp.offer_price > 0) " +
            "ORDER BY sp.price ASC",
            nativeQuery = true)
    List<StoreProduct> findStoreProductsWithValidPrices(@Param("storeId") UUID storeId);

}
