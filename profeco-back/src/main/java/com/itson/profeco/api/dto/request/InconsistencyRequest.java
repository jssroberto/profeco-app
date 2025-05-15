package com.itson.profeco.api.dto.request;

import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InconsistencyRequest {

    @Positive(message = "The published price must be positive")
    @NotNull(message = "The published price cannot be null")
    private Double publishedPrice;

    @Positive(message = "The actual price must be positive")
    @NotNull(message = "The actual price cannot be null")
    private Double actualPrice;

    @NotNull(message = "The store product ID cannot be null")
    private UUID storeProductId;

}
