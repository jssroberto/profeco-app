package com.itson.profeco.api.dto.request;

import com.itson.profeco.model.Inconsistency;
import com.itson.profeco.model.Product;
import com.itson.profeco.model.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

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
