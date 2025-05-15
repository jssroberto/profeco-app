package com.itson.profeco.api.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreProductRequest {

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Product is required")
    private UUID product;
}
