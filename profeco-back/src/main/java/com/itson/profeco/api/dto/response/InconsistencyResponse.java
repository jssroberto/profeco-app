package com.itson.profeco.api.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InconsistencyResponse {

    private UUID id;

    private Double publishedPrice;

    private Double actualPrice;

    private String dateTime;

    private String status;

    private CustomerResponse customer;
    
    private StoreProductResponse storeProduct;

}
