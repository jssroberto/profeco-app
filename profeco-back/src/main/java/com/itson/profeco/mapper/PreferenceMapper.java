package com.itson.profeco.mapper;

import com.itson.profeco.api.dto.request.ProductSearchRequest;
import com.itson.profeco.api.dto.response.FavoriteStoresResponse;
import com.itson.profeco.api.dto.response.ProductSearchResponse;
import com.itson.profeco.api.dto.response.ShoppingListResponse;
import com.itson.profeco.model.Product;
import com.itson.profeco.model.ProductSearch;
import com.itson.profeco.model.Store;
import com.itson.profeco.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.Set;
import org.mapstruct.Named;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.UUID;
import org.mapstruct.Context;


@Mapper(componentModel = "spring")
public interface PreferenceMapper {

    @Mapping(target = "productId", source = "product.id")
    ProductSearchResponse toResponse(ProductSearch productSearch);



    @Named("mapStoresToIds")
    default Set<UUID> mapStoresToIds(Set<Store> stores) {
        if (stores == null || stores.isEmpty()) {
            return Collections.emptySet();
        }
        return stores.stream()
                .map(Store::getId)
                .collect(Collectors.toSet());
    }

    default FavoriteStoresResponse toFavoriteStoresResponse(Set<Store> stores) {
        Set<UUID> storeIds = this.mapStoresToIds(stores);
        return new FavoriteStoresResponse(storeIds);
    }

    @Named("mapProductsToIds")
    default Set<UUID> mapProductsToIds(Set<Product> products) {
        if (products == null || products.isEmpty()) {
            return Collections.emptySet();
        }
        return products.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
    }

    default ShoppingListResponse toShoppingListResponse(Set<Product> products) {
        Set<UUID> productIds = this.mapProductsToIds(products);
        return new ShoppingListResponse(productIds);
    }


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "preference", ignore = true)
    @Mapping(target = "searchCount", ignore = true)
    @Mapping(target = "product", source = "productId", qualifiedByName = "findProductEntityById")
    ProductSearch toEntity(ProductSearchRequest request, @Context ProductRepository productRepository);

    @Named("findProductEntityById")
    default Product findProductEntityById(UUID productId, @Context ProductRepository productRepository) {
        if (productId == null) {
            return null;
        }
        if (productRepository == null) {
            throw new IllegalStateException("ProductRepository must be provided via @Context to findProductEntityById");
        }
        return productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));
    }
}