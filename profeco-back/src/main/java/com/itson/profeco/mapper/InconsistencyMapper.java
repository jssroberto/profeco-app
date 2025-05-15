package com.itson.profeco.mapper;

import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.itson.profeco.api.dto.request.InconsistencyRequest;
import com.itson.profeco.api.dto.response.InconsistencyResponse;
import com.itson.profeco.model.Inconsistency;
import com.itson.profeco.model.StoreProduct;

@Mapper(componentModel = "spring")
public interface InconsistencyMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "publishedPrice", source = "publishedPrice")
    @Mapping(target = "actualPrice", source = "actualPrice")
    @Mapping(target = "dateTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "storeProduct",
            expression = "java(mapStoreProductFromId(request.getStoreProductId()))")
    Inconsistency toEntity(InconsistencyRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "publishedPrice", source = "publishedPrice")
    @Mapping(target = "actualPrice", source = "actualPrice")
    @Mapping(target = "dateTime",
            expression = "java(inconsistency.getDateTime() != null ? inconsistency.getDateTime().toString() : null)")
    @Mapping(target = "status", source = "status.name")
    InconsistencyResponse toResponse(Inconsistency inconsistency);

    default StoreProduct mapStoreProductFromId(UUID storeProductId) {
        if (storeProductId == null) {
            return null;
        }
        StoreProduct storeProduct = new StoreProduct();
        storeProduct.setId(storeProductId);
        return storeProduct;
    }
}
