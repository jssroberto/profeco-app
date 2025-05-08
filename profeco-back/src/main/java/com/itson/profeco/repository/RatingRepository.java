package com.itson.profeco.repository;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itson.profeco.model.Rating;

public interface RatingRepository extends JpaRepository<Rating, UUID> {

}
