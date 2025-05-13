package com.itson.profeco.mapper;

import com.itson.profeco.api.dto.request.WishRequest;
import com.itson.profeco.api.dto.response.WishResponse;
import com.itson.profeco.model.Wish;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface WishMapper {

    @Mapping(source = "store.id", target = "storeId")
    @Mapping(source = "store.name", target = "storeName")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.user.email", target = "customerEmail")
    WishResponse toWishResponse(Wish wish);

    List<WishResponse> toWishResponseList(List<Wish> wishes);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "store", ignore = true)
    @Mapping(target = "customer", ignore = true)
    void updateWishFromRequest(WishRequest wishRequest, @MappingTarget Wish wish);
}