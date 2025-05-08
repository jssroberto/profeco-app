package com.itson.profeco.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.StoreProduct;

public interface StoreProductRepository extends JpaRepository<StoreProduct, UUID> {

}
