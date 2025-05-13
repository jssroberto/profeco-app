package com.itson.profeco.api.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingRequest {

    @NotNull(message = "Score cannot be null.")
    @Min(value = 1, message = "Score must be at least 1.")
    @Max(value = 5, message = "Score must be at most 5.")
    private Integer score;

    @NotBlank(message = "Comment cannot be blank.")
    @Size(max = 250, message = "Comment cannot exceed 250 characters.")
    private String comment;

    @NotNull(message = "Store ID cannot be null.")
    private UUID storeId;
}