package com.itson.profeco.security;

import java.util.Collection;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import lombok.Getter;

@Getter
public class CustomUserDetails extends User {
    private final UUID specificUserId;
    private final String specificName;
    private final UUID genericUserId;

    public CustomUserDetails(String username, String password,
                             Collection<? extends GrantedAuthority> authorities, UUID genericUserId,
                             UUID specificUserId, String specificName) {
        super(username, password, authorities);
        this.genericUserId = genericUserId;
        this.specificUserId = specificUserId;
        this.specificName = specificName;
    }

}