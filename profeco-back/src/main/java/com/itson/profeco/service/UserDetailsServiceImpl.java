package com.itson.profeco.service;

import java.util.stream.Collectors;
import com.itson.profeco.model.*;
import com.itson.profeco.repository.CustomerRepository;
import com.itson.profeco.repository.ProfecoAdminRepository;
import com.itson.profeco.repository.StoreAdminRepository;
import com.itson.profeco.security.CustomUserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.itson.profeco.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final StoreAdminRepository storeAdminRepository;
    private final ProfecoAdminRepository profecoAdminRepository;


    private static final String ROLE_CUSTOMER_AUTH = "ROLE_CUSTOMER";
    private static final String ROLE_STORE_ADMIN_AUTH = "ROLE_STORE_ADMIN";
    private static final String ROLE_PROFECO_ADMIN_AUTH = "ROLE_PROFECO_ADMIN";

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Collection<? extends GrantedAuthority> authorities = getAuthorities(userEntity.getRoles());

        if (authorities.isEmpty()) {
            throw new IllegalStateException("User '" + email + "' has no assigned roles.");
        }

        UserProfileDetails profileDetails = determineUserProfileDetails(userEntity, authorities, email);

        return new CustomUserDetails(
                userEntity.getEmail(),
                userEntity.getPassword(),
                authorities,
                userEntity.getId(),
                profileDetails.specificId(),
                profileDetails.specificName()
        );
    }

    private UserProfileDetails determineUserProfileDetails(UserEntity userEntity,
                                                           Collection<? extends GrantedAuthority> authorities,
                                                           String email) {
        UUID userEntityId = userEntity.getId();

        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(roleName -> roleName.equals(ROLE_CUSTOMER_AUTH) ||
                        roleName.equals(ROLE_STORE_ADMIN_AUTH) ||
                        roleName.equals(ROLE_PROFECO_ADMIN_AUTH))
                .findFirst()
                .map(roleName -> {
                    if (ROLE_CUSTOMER_AUTH.equals(roleName)) {
                        return fetchCustomerDetails(userEntityId, email);
                    } else if (ROLE_STORE_ADMIN_AUTH.equals(roleName)) {
                        return fetchStoreAdminDetails(userEntityId, email);
                    } else if (ROLE_PROFECO_ADMIN_AUTH.equals(roleName)) {
                        return fetchProfecoAdminDetails(userEntityId, email);
                    }
                    return null;
                })
                .orElseThrow(() -> new IllegalStateException(
                        "User '" + email + "' with roles " + authorities +
                                "but without a specific profile defined (Customer, StoreAdmin, ProfecoAdmin)"
                ));
    }

    private UserProfileDetails fetchCustomerDetails(UUID userEntityId, String emailForErrorMessage) {
        Customer customer = customerRepository.findByUser_Id(userEntityId)
                .orElseThrow(() -> SpecificProfileNotFound("Customer", userEntityId, emailForErrorMessage));
        return new UserProfileDetails(customer.getId(), customer.getName());
    }

    private UserProfileDetails fetchStoreAdminDetails(UUID userEntityId, String emailForErrorMessage) {
        StoreAdmin storeAdmin = storeAdminRepository.findByUser_Id(userEntityId)
                .orElseThrow(() -> SpecificProfileNotFound("StoreAdmin", userEntityId, emailForErrorMessage));
        return new UserProfileDetails(storeAdmin.getId(), storeAdmin.getName());
    }

    private UserProfileDetails fetchProfecoAdminDetails(UUID userEntityId, String emailForErrorMessage) {
        ProfecoAdmin profecoAdmin = profecoAdminRepository.findByUser_Id(userEntityId)
                .orElseThrow(() -> SpecificProfileNotFound("ProfecoAdmin", userEntityId, emailForErrorMessage));
        return new UserProfileDetails(profecoAdmin.getId(), profecoAdmin.getName());
    }

    private UsernameNotFoundException SpecificProfileNotFound(String profileType, UUID userEntityId, String email) {
        return new UsernameNotFoundException(
                "Details of " + profileType + " not found for UserEntity with ID: " + userEntityId +
                        " (email: " + email + "). Possible data inconsistency."
        );
    }

    private record UserProfileDetails(UUID specificId, String specificName) {}

    private Collection<? extends GrantedAuthority> getAuthorities(Set<Role> roles) {
        if (roles == null || roles.isEmpty()) {
            return Collections.emptyList();
        }
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()))
                .collect(Collectors.toList());
    }
}