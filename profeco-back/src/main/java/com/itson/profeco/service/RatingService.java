package com.itson.profeco.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.itson.profeco.api.dto.request.RatingRequest;
import com.itson.profeco.api.dto.response.RatingResponse;
import com.itson.profeco.exceptions.OperationNotAllowedException;
import com.itson.profeco.exceptions.ResourceNotFoundException;
import com.itson.profeco.mapper.RatingMapper;
import com.itson.profeco.model.Customer;
import com.itson.profeco.model.Store;
import com.itson.profeco.repository.CustomerRepository;
import com.itson.profeco.repository.StoreRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.itson.profeco.model.Rating;
import com.itson.profeco.repository.RatingRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final RatingMapper ratingMapper;

    @Transactional
    public RatingResponse createRating(RatingRequest ratingRequest, UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));
        Store store = storeRepository.findById(ratingRequest.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store", "id", ratingRequest.getStoreId()));
        Rating rating = ratingMapper.toRatingEntity(ratingRequest);
        rating.setCustomer(customer);
        rating.setStore(store);
        rating.setDate(LocalDate.now());

        Rating savedRating = ratingRepository.save(rating);
        return ratingMapper.toRatingResponse(savedRating);
    }

    @Transactional(readOnly = true)
    public List<RatingResponse> getRatingsForStore(UUID storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new ResourceNotFoundException("Store", "id", storeId);
        }
        List<Rating> ratings = ratingRepository.findByStoreId(storeId);
        return ratingMapper.toRatingResponseList(ratings);
    }

    @Transactional(readOnly = true)
    public List<RatingResponse> getRatingsByCustomer(UUID customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer", "id", customerId);
        }
        List<Rating> ratings = ratingRepository.findByCustomerId(customerId);
        return ratingMapper.toRatingResponseList(ratings);
    }

    @Transactional(readOnly = true)
    public RatingResponse getRatingById(UUID ratingId) {
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating", "id", ratingId));
        return ratingMapper.toRatingResponse(rating);
    }

    @Transactional(readOnly = true)
    public Optional<Double> getAverageRatingScoreForStore(UUID storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new ResourceNotFoundException("Store", "id", storeId);
        }
        Double averageScore = ratingRepository.findAverageScoreByStoreId(storeId);

        return Optional.ofNullable(averageScore);
    }


//    @Transactional
//    public void deleteCustomerRating(UUID ratingId, UUID customerId) {
//        if (!ratingRepository.existsByIdAndCustomerId(ratingId, customerId)) {
//            throw new ResourceNotFoundException("Rating", "id", ratingId + " not found or does not belong to customer " + customerId);
//        }
//        ratingRepository.deleteById(ratingId);
//    }
}