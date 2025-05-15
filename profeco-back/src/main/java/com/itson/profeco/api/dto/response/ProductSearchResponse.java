package com.itson.profeco.api.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.UUID;

@Getter
@Setter
public class ProductSearchResponse {
    private UUID id;
    private UUID productId;
    private BigInteger searchCount;

}