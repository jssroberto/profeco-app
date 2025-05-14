package com.itson.profeco.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.itson.profeco.api.dto.request.ProductRequest;
import com.itson.profeco.api.dto.response.ProductResponse;
import com.itson.profeco.model.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "brandId", target = "brand.id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "imageUrl", ignore = true)
    Product toEntity(ProductRequest request);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "brand.id", target = "brandId")
    ProductResponse toResponse(Product product);
    
}
