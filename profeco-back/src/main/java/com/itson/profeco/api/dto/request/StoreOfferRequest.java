package com.itson.profeco.api.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
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
public class StoreOfferRequest {

    @NotNull(message = "Product ID is required")
    private UUID productId;

    @Positive
    @NotNull(message = "Offer price is required")
    private BigDecimal offerPrice;

    @NotNull(message = "Offer start date is required")
    private LocalDate offerStartDate;

    @NotNull(message = "Offer end date is required")
    private LocalDate offerEndDate;

}

