package com.itson.profeco.api.dto.response;

import java.util.List;
import java.util.UUID;

import com.itson.profeco.security.CustomUserDetails;
import lombok.*;

// AuthResponse.java
import lombok.Builder;
import lombok.Getter;
import java.util.List;
import java.util.UUID;

@Getter
@Builder
public class AuthResponse {

    private final String accessToken;

    private final UUID id;

    private final String email;

    private final String name;

    private final List<String> roles;

    private final UUID userEntityId;

}