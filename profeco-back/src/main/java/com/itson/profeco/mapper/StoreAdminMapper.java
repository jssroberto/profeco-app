package com.itson.profeco.mapper;

import java.util.UUID;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import com.itson.profeco.api.dto.request.StoreAdminRequest;
import com.itson.profeco.api.dto.response.StoreAdminResponse;
import com.itson.profeco.model.Store;
import com.itson.profeco.model.StoreAdmin;

@Mapper(componentModel = "spring")
public interface StoreAdminMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "user.password", source = "password")
    @Mapping(target = "user.id", ignore = true)
    @Mapping(target = "user.roles", ignore = true)
    @Mapping(target = "store", source = "storeId", qualifiedByName = "mapStoreIdToStore")
    StoreAdmin toEntity(StoreAdminRequest request);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "storeId", source = "store.id")
    StoreAdminResponse toResponse(StoreAdmin storeAdmin);

    @Named("mapStoreIdToStore")
    default Store mapStoreIdToStore(UUID storeId) {
        if (storeId == null) {
            return null;
        }
        Store store = new Store();
        store.setId(storeId);
        return store;
    }
}
