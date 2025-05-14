package com.itson.profeco.api.dto.request;

import java.time.LocalDate;
import java.util.UUID;

import lombok.Data;

@Data
public class SaveInconsistencyRequest {
    
    private Double publishedPrice;
    private Double actualPrice;
    private LocalDate date;
    private String status;
    private UUID customerId;
    private UUID storeProductUUID;
}
