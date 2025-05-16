package com.itson.profeco.api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreOfferResponse {

    private BigDecimal offerPrice;

    private LocalDate offerStartDate;

    private LocalDate offerEndDate;

    private List<UUID> inconsistency;

    private StoreProductResponse storeProduct;

}
