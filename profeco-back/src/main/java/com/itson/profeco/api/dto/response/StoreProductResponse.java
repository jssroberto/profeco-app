package com.itson.profeco.api.dto.response;

import com.itson.profeco.model.Product;
import com.itson.profeco.model.Store;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreProductResponse {

    @NotNull(message = "Price is required")
    private BigDecimal price;

    @NotNull(message = "Store is required")
    private UUID storeId;

    @NotNull(message = "Product is required")
    private UUID productId;
}
