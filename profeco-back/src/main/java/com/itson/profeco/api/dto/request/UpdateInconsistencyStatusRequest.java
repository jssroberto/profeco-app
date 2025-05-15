package com.itson.profeco.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInconsistencyStatusRequest {

    @NotBlank(message = "The new status name cannot be blank")
    private String status;
    
}
