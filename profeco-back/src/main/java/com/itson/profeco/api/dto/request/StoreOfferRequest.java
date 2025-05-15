package com.itson.profeco.api.dto.request;

import com.itson.profeco.api.dto.response.StoreProductResponse;
import com.itson.profeco.model.Inconsistency;
import com.itson.profeco.model.StoreProduct;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

