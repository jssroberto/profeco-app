package com.itson.profeco.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @Size(min = 1, max = 50, message = "The new status name must be between 1 and 50 characters")
    private String status;

}
