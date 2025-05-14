package com.itson.profeco.mapper;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.itson.profeco.api.dto.request.StoreRequest;
import com.itson.profeco.api.dto.response.StoreResponse;
import com.itson.profeco.model.Inconsistency;
import com.itson.profeco.model.Rating;
import com.itson.profeco.model.Store;
import com.itson.profeco.model.StoreProduct;
import com.itson.profeco.model.Wish;

@Mapper(componentModel = "spring")
public interface StoreMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    @Mapping(target = "wishes", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    Store toEntity(StoreRequest request);

    @Mapping(target = "productsIds",
            expression = "java(mapStoreProductsToIds(store.getProducts()))")
    @Mapping(target = "ratingsIds", expression = "java(mapRatingsToIds(store.getRatings()))")
    @Mapping(target = "wishesIds", expression = "java(mapWishesToIds(store.getWishes()))")
    StoreResponse toResponse(Store store);

    default Set<UUID> mapStoreProductsToIds(java.util.List<StoreProduct> products) {
        if (products == null)
            return null;
        return products.stream().map(StoreProduct::getId).collect(Collectors.toSet());
    }

    default Set<UUID> mapRatingsToIds(java.util.List<Rating> ratings) {
        if (ratings == null)
            return null;
        return ratings.stream().map(Rating::getId).collect(Collectors.toSet());
    }

    default Set<UUID> mapInconsistenciesToIds(java.util.List<Inconsistency> inconsistencies) {
        if (inconsistencies == null)
            return null;
        return inconsistencies.stream().map(Inconsistency::getId).collect(Collectors.toSet());
    }

    default Set<UUID> mapWishesToIds(java.util.List<Wish> wishes) {
        if (wishes == null)
            return null;
        return wishes.stream().map(Wish::getId).collect(Collectors.toSet());
    }
}
