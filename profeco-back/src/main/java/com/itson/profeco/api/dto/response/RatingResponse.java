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
public class RatingResponse {

    private UUID id;

    private Integer score;

    private String comment;

    private LocalDate date;

    private UUID customerId;

    private String customerName;

}