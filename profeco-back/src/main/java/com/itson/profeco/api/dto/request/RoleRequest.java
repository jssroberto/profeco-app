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
public class RoleRequest {

    @NotBlank(message = "Role name cannot be blank")
    @Size(max = 50, message = "Role name cannot exceed 50 characters")
    private String name;

}
