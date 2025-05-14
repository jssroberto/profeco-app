package com.itson.profeco.api.dto.request;

import java.util.UUID;

import lombok.Data;

@Data
public class UpdateInconsistencyStatusRequest {
    private UUID uuid;
    private String status;
}
