package com.itson.profeco.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.StoreProduct;

public interface StoreProductRepository extends JpaRepository<StoreProduct, UUID> {

    List<StoreProduct> findByStore_Name(String storeName);

    List<StoreProduct> findByProduct_Name(String productName);

    List<StoreProduct> findByPriceGreaterThan(BigDecimal price);

    List<StoreProduct> findByPriceLessThan(BigDecimal price);

    List<StoreProduct> findByPriceBetween(BigDecimal min, BigDecimal max);

    List<StoreProduct> findByPrice(BigDecimal price);

}
