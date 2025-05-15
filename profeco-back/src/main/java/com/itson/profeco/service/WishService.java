package com.itson.profeco.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import com.itson.profeco.api.dto.request.WishRequest;
import com.itson.profeco.api.dto.response.WishResponse;
import com.itson.profeco.exceptions.ResourceNotFoundException;
import com.itson.profeco.mapper.WishMapper;
import com.itson.profeco.model.Customer;
import com.itson.profeco.model.Store;
import com.itson.profeco.repository.CustomerRepository;
import com.itson.profeco.repository.StoreRepository;
import org.springframework.stereotype.Service;
import com.itson.profeco.model.Wish;
import com.itson.profeco.repository.WishRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WishService {


    private final WishRepository wishRepository;
    private final CustomerRepository customerRepository;
    private final StoreRepository storeRepository;
    private final CustomerService customerService;
    private final WishMapper wishMapper;


    @Transactional(readOnly = true)
    public List<WishResponse> getAllWishesForCurrentUser(UUID customerId) {
        List<Wish> wishes = wishRepository.findByCustomerId(customerId);
        return wishMapper.toWishResponseList(wishes);
    }


    @Transactional(readOnly = true)
    public WishResponse getWishByIdForCurrentUser(UUID id, UUID customerId) {
        Wish wish = wishRepository.findByIdAndCustomerId(id, customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Wish", "id", id + " for customer " + customerId));
        return wishMapper.toWishResponse(wish);
    }


    @Transactional
    public WishResponse createWishForCurrentUser(WishRequest wishRequest) {
        UUID customerId = customerService.getAuthenticatedCustomerEntity().getId();
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", customerId));

        Store store = storeRepository.findById(wishRequest.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store", "id", wishRequest.getStoreId()));

        Wish wish = new Wish();
        wishMapper.updateWishFromRequest(wishRequest, wish);
        wish.setCustomer(customer);
        wish.setStore(store);
        wish.setDate(LocalDate.now());


        Wish savedWish = wishRepository.save(wish);


        return wishMapper.toWishResponse(savedWish);
    }


    @Transactional
    public WishResponse updateWishForCurrentUser(UUID id, WishRequest wishRequest) {
        UUID customerId = customerService.getAuthenticatedCustomerEntity().getId();
        Wish existingWish = wishRepository.findByIdAndCustomerId(id, customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Wish", "id", id + " for customer " + customerId));

        Store store = storeRepository.findById(wishRequest.getStoreId())
                .orElseThrow(() -> new ResourceNotFoundException("Store", "id", wishRequest.getStoreId()));


        wishMapper.updateWishFromRequest(wishRequest, existingWish);
        existingWish.setStore(store);


        Wish updatedWish = wishRepository.save(existingWish);


        return wishMapper.toWishResponse(updatedWish);
    }

    @Transactional
    public void deleteWishForCurrentUser(UUID id) {
        UUID customerId = customerService.getAuthenticatedCustomerEntity().getId();
        if (!wishRepository.existsByIdAndCustomerId(id, customerId)) {
            throw new ResourceNotFoundException("Wish", "id", id + " for customer " + customerId);
        }

        wishRepository.deleteById(id);
    }


    @Transactional(readOnly = true)
    public List<WishResponse> getAllWishesForStore(UUID storeId) {
        List<Wish> wishes = wishRepository.findByStoreId(storeId);
        return wishMapper.toWishResponseList(wishes);
    }

    @Transactional(readOnly = true)
    public WishResponse getWishByIdForStore(UUID wishId, UUID storeId) {
        Wish wish = wishRepository.findByIdAndStoreId(wishId, storeId)
                .orElseThrow(() -> new ResourceNotFoundException("Wish", "id", wishId + " for store " + storeId));
        return wishMapper.toWishResponse(wish);
    }


    @Transactional
    public void deleteWishForStore(UUID wishId, UUID storeId) {
        if (!wishRepository.existsByIdAndStoreId(wishId, storeId)) {
            throw new ResourceNotFoundException("Wish", "id", wishId + " for store " + storeId);
        }
        wishRepository.deleteById(wishId);
    }
}