package com.itson.profeco.api.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreOfferRequest {

    @NotNull(message = "The category ID is required")
    private BigDecimal offerPrice;

    @NotNull(message = "The category ID is required")
    private LocalDate offerStartDate;

    @NotNull(message = "The category ID is required")
    private LocalDate offerEndDate;


}

