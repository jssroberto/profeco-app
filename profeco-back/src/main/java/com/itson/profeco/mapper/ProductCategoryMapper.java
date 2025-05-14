package com.itson.profeco.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.itson.profeco.api.dto.request.ProductCategoryRequest;
import com.itson.profeco.api.dto.response.ProductCategoryResponse;
import com.itson.profeco.model.ProductCategory;

@Mapper(componentModel = "spring")
public interface ProductCategoryMapper {

    @Mapping(target = "id", ignore = true)
    ProductCategory toEntity(ProductCategoryRequest request);

    ProductCategoryResponse toResponse(ProductCategory category);
    
}
