package com.itson.profeco.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.ProductCategory;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {

}
