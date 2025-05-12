package com.itson.profeco.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.itson.profeco.api.dto.response.AuthResponse;
import com.itson.profeco.api.dto.response.CustomerResponse;
import com.itson.profeco.api.dto.response.ProfecoAdminResponse;
import com.itson.profeco.api.dto.response.StoreAdminResponse;

@Mapper(componentModel = "spring")
public interface AuthMapper {

    @Mapping(target = "accessToken", ignore = true)
    AuthResponse fromCustomerResponse(CustomerResponse customerResponse);

    @Mapping(target = "accessToken", ignore = true)
    AuthResponse fromProfecoAdminResponse(ProfecoAdminResponse profecoAdminResponse);

    @Mapping(target = "accessToken", ignore = true)
    AuthResponse fromStoreAdminResponse(StoreAdminResponse storeAdminResponse);
    
}
