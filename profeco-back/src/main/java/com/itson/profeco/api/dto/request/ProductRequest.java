package com.itson.profeco.api.dto.request;

import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "The name is required")
    @Size(max = 100, message = "The name should not exceed 100 characters")
    private String name;

    @NotNull(message = "The category ID is required")
    private UUID categoryId;

    @NotNull(message = "The brand ID is required")
    private UUID brandId;

}
