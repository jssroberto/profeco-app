package com.itson.profeco.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.Rating;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;


@Repository
public interface RatingRepository extends JpaRepository<Rating, UUID> {

    List<Rating> findByStoreId(UUID storeId);

    List<Rating> findByCustomerId(UUID customerId);

//    Optional<Rating> findByIdAndCustomerId(UUID ratingId, UUID customerId);
//
//    boolean existsByIdAndCustomerId(UUID ratingId, UUID customerId);
//
     @Query("SELECT AVG(r.score) FROM Rating r WHERE r.store.id = :storeId")
     Double findAverageScoreByStoreId(@Param("storeId") UUID storeId);

}