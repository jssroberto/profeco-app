package com.itson.profeco.mapper;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.itson.profeco.api.dto.request.CustomerRequest;
import com.itson.profeco.api.dto.response.CustomerResponse;
import com.itson.profeco.model.Inconsistency;
import com.itson.profeco.model.Rating;
import com.itson.profeco.model.Role;
import com.itson.profeco.model.Customer;
import com.itson.profeco.model.Wish;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "preference", ignore = true)
    @Mapping(target = "ratings", ignore = true)
    @Mapping(target = "inconsistencies", ignore = true)
    @Mapping(target = "wishes", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Customer toEntity(CustomerRequest request);

    @Mapping(target = "preferenceId", source = "preference.id")
    @Mapping(target = "ratingsIds", expression = "java(mapRatingsToIds(customer.getRatings()))")
    @Mapping(target = "inconsistenciesIds",
            expression = "java(mapInconsistenciesToIds(customer.getInconsistencies()))")
    @Mapping(target = "wishesIds", expression = "java(mapWishesToIds(customer.getWishes()))")
    @Mapping(target = "rolesIds", expression = "java(mapRolesToIds(customer.getRoles()))")
    CustomerResponse toResponse(Customer customer);

    default Set<UUID> mapRatingsToIds(java.util.List<Rating> ratings) { // Changed method name for
                                                                        // clarity and consistency
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

    default Set<UUID> mapRolesToIds(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream().map(Role::getId).collect(Collectors.toSet());
    }
}
