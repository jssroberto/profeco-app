package com.itson.profeco.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.Inconsistency;

public interface InconsistencyRepository extends JpaRepository<Inconsistency, UUID> {

    List<Inconsistency> findByCustomerId(UUID customerId);

    List<Inconsistency> findByStoreProductId(UUID storeProductId);

    List<Inconsistency> findByStoreProductStoreId(UUID storeId);

    Integer countByStoreProductStoreId(UUID storeId);
}
