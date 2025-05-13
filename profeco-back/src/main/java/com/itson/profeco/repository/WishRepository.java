package com.itson.profeco.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.Wish;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, UUID> {

    List<Wish> findByCustomerId(UUID customerId);

    Optional<Wish> findByIdAndCustomerId(UUID id, UUID customerId);

    boolean existsByIdAndCustomerId(UUID id, UUID customerId);

    List<Wish> findByStoreId(UUID storeId);

    Optional<Wish> findByIdAndStoreId(UUID id, UUID storeId);

    boolean existsByIdAndStoreId(UUID id, UUID storeId);

}