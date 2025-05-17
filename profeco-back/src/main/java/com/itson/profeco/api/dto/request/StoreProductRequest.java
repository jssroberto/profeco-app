package com.itson.profeco.api.dto.request;


import java.math.BigDecimal;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreProductRequest {

    @Positive
    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Product ID is required")
    private UUID productId;
}
