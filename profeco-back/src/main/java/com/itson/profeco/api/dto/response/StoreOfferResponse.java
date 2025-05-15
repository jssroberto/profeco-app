package com.itson.profeco.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreOfferResponse {
    @NotNull(message = "The category ID is required")
    private BigDecimal offerPrice;

    @NotNull(message = "The category ID is required")
    private LocalDate offerStartDate;

    @NotNull(message = "The category ID is required")
    private LocalDate offerEndDate;

    @NotNull(message = "The category ID is required")
    private List<UUID> inconsistency;

    @NotNull(message = "The category ID is required")
    private StoreProductResponse storeProduct;
}
