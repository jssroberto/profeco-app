package com.itson.profeco.api.dto.response;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponse {

    private UUID id;

    private String name;

    private String email;

    private UUID preferenceId;

    private List<UUID> ratingIds;

    private List<UUID> inconsistencyIds;

    private List<UUID> wishIds;

}
