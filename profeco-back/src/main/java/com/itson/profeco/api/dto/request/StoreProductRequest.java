package com.itson.profeco.api.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.itson.profeco.model.Inconsistency;
import com.itson.profeco.model.Product;
import com.itson.profeco.model.Store;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

public class StoreProductRequest {

    @NotNull(message = "Price is required")
    private BigDecimal price;

    private BigDecimal offerPrice;

    private LocalDate offerStartDate;

    private LocalDate offerEndDate;

    @ManyToOne
    private Inconsistency inconsistency;

    @ManyToOne
    private Store store;

    @ManyToOne
    private Product product;
}
