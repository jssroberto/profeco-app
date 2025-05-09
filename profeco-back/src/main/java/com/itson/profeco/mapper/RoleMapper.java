package com.itson.profeco.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.itson.profeco.api.dto.request.RoleRequest;
import com.itson.profeco.api.dto.response.RoleResponse;
import com.itson.profeco.model.Role;

@Mapper(componentModel = "spring")
public interface RoleMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customers", ignore = true)
    Role toEntity(RoleRequest roleRequest);

    RoleResponse toResponse(Role role);

}
