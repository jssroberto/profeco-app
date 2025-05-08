package com.itson.profeco.api.dto.response;

import java.util.Set;
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

    private Set<UUID> ratingsIds;

    private Set<UUID> inconsistenciesIds;

    private Set<UUID> wishesIds;

}
