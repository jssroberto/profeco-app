package com.itson.profeco.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import com.itson.profeco.model.Store; // Added import for Store
import com.itson.profeco.repository.CustomerRepository;
import com.itson.profeco.repository.PreferenceRepository;
import com.itson.profeco.repository.ProductRepository;
import com.itson.profeco.repository.ProductSearchRepository;
import com.itson.profeco.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
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
    public FavoriteStoresResponse addFavoriteStore(UUID storeId) {
        UUID customerId = jwtService.getCurrentCustomer().getId();
        Preference preference = getOrCreatePreference(customerId);
        Store store = storeRepository.findById(storeId).orElseThrow(
                () -> new EntityNotFoundException("Store not found with ID: " + storeId));

        // .add() for Set returns true if the element was added, false if it was already present.
        if (!preference.getFavoriteStores().add(store)) {
            throw new IllegalStateException("Store with ID " + storeId + " is already a favorite.");
        }
        preferenceRepository.save(preference); // Save the preference entity after modification
        return preferenceMapper.toFavoriteStoresResponse(preference.getFavoriteStores());
    }


    @Transactional
    public ShoppingListResponse addProductToShoppingList(UUID productId) {
        UUID customerId = jwtService.getCurrentCustomer().getId();
        Preference preference = getOrCreatePreference(customerId);
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new EntityNotFoundException("Product not found with ID: " + productId));

        if (!preference.getShoppingList().add(product)) {
            throw new IllegalStateException(
                    "Product with ID " + productId + " is already in the shopping list.");
        }
        preferenceRepository.save(preference); // Save the preference entity
        return preferenceMapper.toShoppingListResponse(preference.getShoppingList());
    }

    @Transactional
    public ShoppingListResponse removeProductFromShoppingList(UUID productId) {
        UUID customerId = jwtService.getCurrentCustomer().getId();
        Preference preference = preferenceRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Preferences not found for customer ID: " + customerId));

        boolean removed = preference.getShoppingList().removeIf(p -> p.getId().equals(productId));
        if (!removed) {
            throw new EntityNotFoundException(
                    "Product with ID " + productId + " not found in shopping list.");
        }
        preferenceRepository.save(preference);
        return preferenceMapper.toShoppingListResponse(preference.getShoppingList());
    }

    @Transactional
    public FavoriteStoresResponse removeFavoriteStore(UUID storeId) {
        UUID customerId = jwtService.getCurrentCustomer().getId();
        Preference preference = preferenceRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Preferences not found for customer ID: " + customerId));

        boolean removed = preference.getFavoriteStores().removeIf(s -> s.getId().equals(storeId));
        if (!removed) {
            throw new EntityNotFoundException(
                    "Store with ID " + storeId + " not found in favorite stores.");
        }
        preferenceRepository.save(preference);
        return preferenceMapper.toFavoriteStoresResponse(preference.getFavoriteStores());
    }

    public FavoriteStoresResponse getFavoriteStores() {
        UUID customerId = jwtService.getCurrentCustomer().getId();
        Preference preference = getOrCreatePreference(customerId); // Ensures preference exists
        return preferenceMapper.toFavoriteStoresResponse(preference.getFavoriteStores());
    }

    public ShoppingListResponse getShoppingList() {
        UUID customerId = jwtService.getCurrentCustomer().getId();
        Preference preference = getOrCreatePreference(customerId); // Ensures preference exists
        return preferenceMapper.toShoppingListResponse(preference.getShoppingList());
    }

    @Transactional
    public ProductSearchResponse addOrUpdateProductSearch(ProductSearchRequest request) {

        UUID customerId = jwtService.getCurrentCustomer().getId();
        Objects.requireNonNull(request.getProductId(), "productId cannot be null");

        Preference preference = getOrCreatePreference(customerId);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Product not found with ID: " + request.getProductId()));

        ProductSearch productSearch = preference.getProductSearches().stream()
                .filter(ps -> ps.getProduct().getId().equals(request.getProductId())).findFirst()
                .orElseGet(() -> {
                    ProductSearch newSearch = new ProductSearch();
                    newSearch.setPreference(preference);
                    newSearch.setProduct(product);
                    newSearch.setSearchCount(BigInteger.ZERO);
                    preference.getProductSearches().add(newSearch); // Add to the collection in
                                                                    // Preference
                    // No need to save productSearchRepository here if cascade is set up from
                    // Preference to ProductSearch
                    // However, explicit save is fine if newSearch is not yet managed or to ensure
                    // ID generation
                    return newSearch;
                });

        productSearch.setSearchCount(productSearch.getSearchCount().add(BigInteger.ONE));
        productSearchRepository.save(productSearch); // Save ProductSearch explicitly
        // preferenceRepository.save(preference); // Consider if this is needed based on cascade
        // settings

        return preferenceMapper.toResponse(productSearch);
    }

    public List<ProductSearchResponse> getProductSearches(Optional<Integer> limit) {
        UUID customerId = jwtService.getCurrentCustomer().getId();
        Preference preference = getOrCreatePreference(customerId);

        // Ensure productSearches is not null, which getOrCreatePreference should handle
        List<ProductSearch> searches = new ArrayList<>(preference.getProductSearches());
        searches.sort(Comparator.comparing(ProductSearch::getSearchCount).reversed());

        List<ProductSearch> limitedSearches = limit.filter(l -> l > 0)
                .map(l -> searches.subList(0, Math.min(l, searches.size()))).orElse(searches);

        return limitedSearches.stream().map(preferenceMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductSearchResponse> getAllProductSearchesSortedByCountDesc() {
        UUID customerId = jwtService.getCurrentCustomer().getId();
        Preference preference = getOrCreatePreference(customerId);

        return preference.getProductSearches().stream()
                .sorted(Comparator.comparing(ProductSearch::getSearchCount).reversed())
                .map(preferenceMapper::toResponse).collect(Collectors.toList());
    }

    private Preference getOrCreatePreference(UUID customerId) {
        return preferenceRepository.findByCustomerId(customerId).orElseGet(() -> {
            Customer customer = customerRepository.findByUser_Id(customerId).orElseThrow(
                    () -> new EntityNotFoundException("Customer not found with User ID: "
                            + customerId + " for preference creation."));

            Preference newPreference = new Preference();
            newPreference.setCustomer(customer);
            // Initialize collections to prevent NullPointerExceptions later
            newPreference.setFavoriteStores(new HashSet<>());
            newPreference.setShoppingList(new HashSet<>());
            // Assuming ProductSearch is a Set in Preference model based on common usage,
            // but if setter expects List, this needs to be ArrayList.
            // The error was: setProductSearches(List<ProductSearch>) is not applicable for
            // (HashSet<>)
            // So, it should be a List for the setter, but getProductSearches() might return Set.
            // Let's assume the getter returns a Collection that can be initialized with ArrayList
            // for the setter's sake if direct field access is not used.
            // If Preference.productSearches is a List, then new ArrayList<>() is correct.
            // If Preference.productSearches is a Set, and setProductSearches expects a List, this
            // is an inconsistency in Preference model.
            // For now, satisfying the setter as per the error message:
            newPreference.setProductSearches(new ArrayList<>());
            return preferenceRepository.save(newPreference);
        });
    }
}
