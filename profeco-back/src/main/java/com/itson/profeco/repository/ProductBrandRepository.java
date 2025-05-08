package com.itson.profeco.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.ProductBrand;

public interface ProductBrandRepository extends JpaRepository<ProductBrand, UUID> {

}
