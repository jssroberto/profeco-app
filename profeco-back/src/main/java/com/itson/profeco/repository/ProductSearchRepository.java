package com.itson.profeco.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.ProductSearch;

public interface ProductSearchRepository extends JpaRepository<ProductSearch, UUID> {

}
