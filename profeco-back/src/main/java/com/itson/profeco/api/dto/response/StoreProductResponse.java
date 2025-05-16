package com.itson.profeco.api.dto.response;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreProductResponse {

    private UUID id;

    private BigDecimal price;

    private BigDecimal offerPrice;

    private String offerStartDate;

    private String offerEndDate;

    private UUID storeId;

    private UUID productId;
    
}
