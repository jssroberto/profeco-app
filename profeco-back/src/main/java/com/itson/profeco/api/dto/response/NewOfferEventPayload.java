package com.itson.profeco.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewOfferEventPayload {
    private UUID storeProductId;
    private UUID storeId;
    private String storeName;
    private UUID productId;
    private String productName;
    private BigDecimal offerPrice;
    private LocalDate offerStartDate;
    private LocalDate offerEndDate;
    private String offerDescription;
}