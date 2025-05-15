package com.itson.profeco.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.request.ProductSearchRequest;
import com.itson.profeco.api.dto.response.FavoriteStoresResponse;
import com.itson.profeco.api.dto.response.ProductSearchResponse;
import com.itson.profeco.api.dto.response.ShoppingListResponse;
import com.itson.profeco.mapper.PreferenceMapper;
import com.itson.profeco.model.Customer;
import com.itson.profeco.model.Preference;
import com.itson.profeco.model.Product;
import com.itson.profeco.model.ProductSearch;
import com.itson.profeco.repository.CustomerRepository;
import com.itson.profeco.repository.PreferenceRepository;
import com.itson.profeco.repository.ProductRepository;
import com.itson.profeco.repository.ProductSearchRepository;
import com.itson.profeco.repository.StoreRepository;
// Import your DTOs and Entities
// e.g., import com.example.dto.*;
// e.g., import com.example.entity.*;
import jakarta.persistence.EntityNotFoundException; // Or javax.persistence if using older JPA
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PreferenceService {

    private final PreferenceRepository preferenceRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;
    private final PreferenceMapper preferenceMapper;
    private final CustomerRepository customerRepository;
    private final CustomerService jwtService;
    private final ProductSearchRepository productSearchRepository;


    @Transactional
    public Optional<FavoriteStoresResponse> addFavoriteStore(UUID storeId) {
        UUID customerId = jwtService.getCurrentCustomer().getId();
        return preferenceRepository.findByCustomerId(customerId).map(preference -> {
            return storeRepository.findById(storeId).map(store -> {
                if (preference.getFavoriteStores().stream()
                        .anyMatch(s -> s.getId().equals(storeId))) {
                    return Optional.<FavoriteStoresResponse>empty();
                }
                preference.getFavoriteStores().add(store);
                preferenceRepository.save(preference);
                return Optional.of(
                        preferenceMapper.toFavoriteStoresResponse(preference.getFavoriteStores()));
            }).orElse(Optional.empty());
        }).orElseGet(() -> createPreferenceAndAddStore(customerId, storeId));
    }


    @Transactional
    public Optional<ShoppingListResponse> addProductToShoppingList(UUID productId) {
        UUID customerId = jwtService.getCurrentCustomer().getId();
        return preferenceRepository.findByCustomerId(customerId).map(preference -> {
            return productRepository.findById(productId).map(product -> {
                if (preference.getShoppingList().stream()
                        .anyMatch(p -> p.getId().equals(productId))) {
                    return Optional.<ShoppingListResponse>empty();
                }
                preference.getShoppingList().add(product);
                preferenceRepository.save(preference);
                return Optional
                        .of(preferenceMapper.toShoppingListResponse(preference.getShoppingList()));
            }).orElse(Optional.empty());
        }).orElseGet(() -> createPreferenceAndAddProduct(customerId, productId));
    }

    @Transactional
    public Optional<ShoppingListResponse> removeProductFromShoppingList(UUID productId) {
        UUID customerId = jwtService.getCurrentCustomer().getId();
        return preferenceRepository.findByCustomerId(customerId).map(preference -> {
            boolean removed =
                    preference.getShoppingList().removeIf(p -> p.getId().equals(productId));
            if (removed) {
                preferenceRepository.save(preference);
                return Optional
                        .of(preferenceMapper.toShoppingListResponse(preference.getShoppingList()));
            }
            return Optional.<ShoppingListResponse>empty();
        }).orElse(Optional.empty());
    }

    @Transactional
    public Optional<FavoriteStoresResponse> removeFavoriteStore(UUID storeId) {
        UUID customerId = jwtService.getCurrentCustomer().getId();
        return preferenceRepository.findByCustomerId(customerId).map(preference -> {
            boolean removed =
                    preference.getFavoriteStores().removeIf(s -> s.getId().equals(storeId));
            if (removed) {
                preferenceRepository.save(preference);
                return Optional.of(
                        preferenceMapper.toFavoriteStoresResponse(preference.getFavoriteStores()));
            }
            return Optional.<FavoriteStoresResponse>empty();
        }).orElse(Optional.empty());
    }

    public FavoriteStoresResponse getFavoriteStores() {
        UUID customerId = jwtService.getCurrentCustomer().getId();
        return preferenceRepository.findByCustomerId(customerId)
                .map(preference -> preferenceMapper
                        .toFavoriteStoresResponse(preference.getFavoriteStores()))
                .orElseGet(() -> new FavoriteStoresResponse(Collections.emptySet()));
    }

    public ShoppingListResponse getShoppingList() {
        UUID customerId = jwtService.getCurrentCustomer().getId();
        return preferenceRepository.findByCustomerId(customerId).map(
                preference -> preferenceMapper.toShoppingListResponse(preference.getShoppingList()))
                .orElseGet(() -> new ShoppingListResponse(Collections.emptySet()));
    }

    @Transactional
    public ProductSearchResponse addOrUpdateProductSearch(ProductSearchRequest request) {

        UUID customerId = jwtService.getCurrentCustomer().getId();
        Objects.requireNonNull(request.getProductId(), "productId cannot be null");

        Preference preference = getOrCreatePreference(customerId);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        ProductSearch productSearch = preference.getProductSearches().stream()
                .filter(ps -> ps.getProduct().getId().equals(request.getProductId())).findFirst()
                .orElseGet(() -> {
                    ProductSearch newSearch = new ProductSearch();
                    newSearch.setPreference(preference);
                    newSearch.setProduct(product);
                    newSearch.setSearchCount(BigInteger.ZERO);
                    preference.getProductSearches().add(newSearch);
                    return newSearch;
                });

        productSearch.setSearchCount(productSearch.getSearchCount().add(BigInteger.ONE));

        productSearchRepository.save(productSearch);

        return preferenceMapper.toResponse(productSearch);
    }

    public List<ProductSearchResponse> getProductSearches(Optional<Integer> limit) {
        UUID customerId = jwtService.getCurrentCustomer().getId();
        return preferenceRepository.findByCustomerId(customerId).map(preference -> {
            List<ProductSearch> searches = new ArrayList<>(preference.getProductSearches());
            searches.sort(Comparator.comparing(ProductSearch::getSearchCount).reversed());

            return limit.filter(l -> l > 0)
                    .map(l -> searches.subList(0, Math.min(l, searches.size()))).orElse(searches)
                    .stream().map(preferenceMapper::toResponse).toList();
        }).orElse(Collections.emptyList());
    }

    @Transactional(readOnly = true)
    public List<ProductSearchResponse> getAllProductSearchesSortedByCountDesc() {
        UUID customerId = jwtService.getCurrentCustomer().getId();

        return preferenceRepository.findByCustomerId(customerId)
                .map(preference -> preference.getProductSearches().stream()
                        .sorted(Comparator.comparing(ProductSearch::getSearchCount).reversed())
                        .map(preferenceMapper::toResponse).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }


    private Optional<FavoriteStoresResponse> createPreferenceAndAddStore(UUID customerId,
            UUID storeId) {
        return customerRepository.findByUser_Id(customerId).map(customer -> {
            Preference newPreference = new Preference();
            newPreference.setCustomer(customer);

            return storeRepository.findById(storeId).map(store -> {
                newPreference.setFavoriteStores(new HashSet<>(Set.of(store)));
                Preference savedPreference = preferenceRepository.save(newPreference);
                return Optional.of(preferenceMapper
                        .toFavoriteStoresResponse(savedPreference.getFavoriteStores()));
            }).orElse(Optional.empty());
        }).orElse(Optional.empty());
    }

    private Optional<ShoppingListResponse> createPreferenceAndAddProduct(UUID customerId,
            UUID productId) {
        return customerRepository.findByUser_Id(customerId).map(customer -> {
            Preference newPreference = new Preference();
            newPreference.setCustomer(customer);

            return productRepository.findById(productId).map(product -> {
                newPreference.setShoppingList(new HashSet<>(Set.of(product)));
                Preference savedPreference = preferenceRepository.save(newPreference);
                return Optional.of(
                        preferenceMapper.toShoppingListResponse(savedPreference.getShoppingList()));
            }).orElse(Optional.empty());
        }).orElse(Optional.empty());
    }

    private Preference getOrCreatePreference(UUID customerId) {
        return preferenceRepository.findByCustomerId(customerId).orElseGet(() -> {
            Customer customer = customerRepository.findByUser_Id(customerId)
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

            Preference newPreference = new Preference();
            newPreference.setCustomer(customer);
            return preferenceRepository.save(newPreference);
        });
    }
}
