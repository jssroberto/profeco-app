package com.itson.profeco.mapper;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.itson.profeco.api.dto.request.CustomerRequest;
import com.itson.profeco.api.dto.response.CustomerResponse;
import com.itson.profeco.model.Customer;
import com.itson.profeco.model.Inconsistency;
import com.itson.profeco.model.Rating;
import com.itson.profeco.model.Wish;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "preference", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    @Mapping(target = "inconsistencies", ignore = true)
    @Mapping(target = "wishes", ignore = true)
    Customer toEntity(CustomerRequest request);

    @Mapping(target = "preferenceId", source = "preference.id")
    @Mapping(target = "ratingsIds", expression = "java(mapRatingsIds(customer))")
    @Mapping(target = "inconsistenciesIds", expression = "java(mapInconsistenciesIds(customer))")
    @Mapping(target = "wishesIds", expression = "java(mapWishesIds(customer))")
    CustomerResponse toResponse(Customer customer);

    default Set<UUID> mapRatingsIds(Customer customer) {
        if (customer.getRatings() == null)
            return null;
        return customer.getRatings().stream().map(Rating::getId).collect(Collectors.toSet());
    }

    default Set<UUID> mapInconsistenciesIds(Customer customer) {
        if (customer.getInconsistencies() == null)
            return null;
        return customer.getInconsistencies().stream().map(Inconsistency::getId)
                .collect(Collectors.toSet());
    }

    default Set<UUID> mapWishesIds(Customer customer) {
        if (customer.getWishes() == null)
            return null;
        return customer.getWishes().stream().map(Wish::getId).collect(Collectors.toSet());
    }
}
