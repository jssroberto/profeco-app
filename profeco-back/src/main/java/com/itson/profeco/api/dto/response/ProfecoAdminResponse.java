package com.itson.profeco.api.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfecoAdminResponse {

    private UUID id;

    private UUID userId;

    private String email;

    private String name;

}
