package com.itson.profeco.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.itson.profeco.api.dto.request.ProfecoAdminRequest;
import com.itson.profeco.api.dto.response.ProfecoAdminResponse;
import com.itson.profeco.model.ProfecoAdmin;

@Mapper(componentModel = "spring")
public interface ProfecoAdminMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "user.password", source = "password")
    @Mapping(target = "user.id", ignore = true)
    @Mapping(target = "user.roles", ignore = true)
    ProfecoAdmin toEntity(ProfecoAdminRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "userId", source = "user.id")
    ProfecoAdminResponse toResponse(ProfecoAdmin profecoAdmin);
}
