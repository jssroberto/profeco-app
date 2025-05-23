package com.itson.profeco.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.ProductBrand;

public interface ProductBrandRepository extends JpaRepository<ProductBrand, UUID> {

    Optional<ProductBrand> findByName(String name);

}
