package com.itson.profeco.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WishResponse {

    private UUID id;
    private String description;
    private LocalDate date;
    private UUID storeId;
    private String storeName;
    private UUID customerId;
    private String customerEmail;

}