package com.itson.profeco.api.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvitationCodeResponse {

    private String code;

    private String roleName;
    
    private UUID roleId;

}
