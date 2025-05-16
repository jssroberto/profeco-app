package com.itson.profeco.api.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchRequest {

    @NotNull(message = "Product ID cannot be null")
    UUID productId;

}
