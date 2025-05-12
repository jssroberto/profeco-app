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
    @Builder.Default private final String name = "";
    private final List<String> roles;
    private final UUID userEntityId;


    public static AuthResponse from(CustomUserDetails user, String token) {
        AuthResponseBuilder builder = AuthResponse.builder()
                .accessToken(token)
                .id(user.getSpecificUserId())
                .email(user.getUsername())
                .name(user.getSpecificName() != null ? user.getSpecificName() : "")
                .roles(user.getAuthorities().stream()
                        .map(a -> a.getAuthority().replace("ROLE_", ""))
                        .toList());


        if (user.getGenericUserId() != null) {
            builder.userEntityId(user.getGenericUserId());
        } else {

            throw new IllegalStateException("GenericUserId cannot be null in CustomUserDetails when constructing AuthResponse.");
        }


        return builder.build();
    }
}