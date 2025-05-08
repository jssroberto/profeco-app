package com.itson.profeco.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.itson.profeco.model.Rating;
import com.itson.profeco.repository.RatingRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public Rating getRatingById(UUID id) {
        return ratingRepository.findById(id).orElse(null);
    }

    public Rating saveRating(Rating rating) {
        return ratingRepository.save(rating);
    }

    public Rating updateRating(UUID id, Rating updatedRating) {
        if (!ratingRepository.existsById(id)) {
            return null;
        }
        updatedRating.setId(id);
        return ratingRepository.save(updatedRating);
    }

    public void deleteRating(UUID id) {
        ratingRepository.deleteById(id);
    }
}
