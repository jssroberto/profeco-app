package com.itson.profeco.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.Preference;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PreferenceRepository extends JpaRepository<Preference, UUID> {

    @Query("SELECT p FROM Preference p WHERE p.customer.id = :customerId")
    Optional<Preference> findByCustomerId(@Param("customerId") UUID customerId);

    @Query("SELECT p FROM Preference p JOIN p.customer c WHERE c.user.id = :userId")
    Optional<Preference> findByUserId(@Param("userId") UUID userId);

    List<Preference> findByFavoriteStores_Id(UUID storeId);
}
