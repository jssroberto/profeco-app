package com.itson.profeco.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.itson.profeco.api.dto.request.ProductBrandRequest;
import com.itson.profeco.api.dto.response.ProductBrandResponse;
import com.itson.profeco.model.ProductBrand;

@Mapper(componentModel = "spring")
public interface ProductBrandMapper {

    @Mapping(target = "id", ignore = true)
    ProductBrand toEntity(ProductBrandRequest request);

    ProductBrandResponse toResponse(ProductBrand productBrand);

}
