package com.itson.profeco.mapper;

import com.itson.profeco.api.dto.request.StoreOfferRequest;
import com.itson.profeco.api.dto.request.StoreProductRequest;
import com.itson.profeco.api.dto.response.StoreOfferResponse;
import com.itson.profeco.api.dto.response.StoreProductResponse;
import com.itson.profeco.model.Inconsistency;
import com.itson.profeco.model.Product;
import com.itson.profeco.model.Store;
import com.itson.profeco.model.StoreProduct;
import org.mapstruct.*;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StoreProductMapper {


    @Mapping(target = "storeId", source = "entity.store.id")
    @Mapping(target = "productId", source = "entity.product.id")
    StoreProductResponse entityToProductResponse(StoreProduct entity);


    @Mapping(target = "storeProduct", source = "entity")
    @Mapping(target = "inconsistency", source = "entity.inconsistency", qualifiedByName = "mapInconsistencyObjectToUuidList")
    StoreOfferResponse entityToOfferResponse(StoreProduct entity);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "offerPrice", ignore = true)
    @Mapping(target = "offerStartDate", ignore = true)
    @Mapping(target = "offerEndDate", ignore = true)
    @Mapping(target = "inconsistency", ignore = true)
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "product", ignore = true)
    StoreProduct productRequestToEntity(StoreProductRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "offerPrice", ignore = true)
    @Mapping(target = "offerStartDate", ignore = true)
    @Mapping(target = "offerEndDate", ignore = true)
    @Mapping(target = "inconsistency", ignore = true)
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "product", ignore = true)
    void updateEntityFromProductRequest(@MappingTarget StoreProduct entity, StoreProductRequest request);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "inconsistency", ignore = true)
    void updateOfferFieldsInStoreProduct(@MappingTarget StoreProduct entity, StoreOfferRequest request);


    default Store mapUuidToStoreObject(UUID storeId) {
        if (storeId == null) {
            return null;
        }
        Store store = new Store();
        store.setId(storeId);
        return store;
    }

    default Product mapUuidToProductObject(UUID productId) {
        if (productId == null) {
            return null;
        }
        Product product = new Product();
        product.setId(productId);
        return product;
    }

    @Named("mapInconsistencyObjectToUuidList")
    default List<UUID> mapInconsistencyObjectToUuidList(Inconsistency inconsistency) {
        if (inconsistency == null || inconsistency.getId() == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(inconsistency.getId());
    }
}