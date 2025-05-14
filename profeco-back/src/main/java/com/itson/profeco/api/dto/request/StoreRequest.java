package com.itson.profeco.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 50, message = "Name should not exceed 50 characters")
    private String name;

    @NotBlank(message = "Location is required")
    @Size(max = 150, message = "Location should not exceed 150 characters")
    private String location;

}