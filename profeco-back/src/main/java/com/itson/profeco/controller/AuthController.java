package com.itson.profeco.controller;

import com.itson.profeco.security.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.api.dto.request.AuthRequest;
import com.itson.profeco.api.dto.request.CustomerRequest;
import com.itson.profeco.api.dto.request.ProfecoAdminRequest;
import com.itson.profeco.api.dto.request.StoreAdminRequest;
import com.itson.profeco.api.dto.response.AuthResponse;
import com.itson.profeco.security.JwtUtil;
import com.itson.profeco.service.CustomerService;
import com.itson.profeco.service.InvitationCodeService;
import com.itson.profeco.service.ProfecoAdminService;
import com.itson.profeco.service.StoreAdminService;
import com.itson.profeco.service.UserDetailsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;

import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for authentication and registration")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final CustomerService customerService;
    private final StoreAdminService storeAdminService;
    private final ProfecoAdminService profecoAdminService;
    private final InvitationCodeService invitationCodeService;


    @Operation(summary = "Authenticate user and return JWT",
            description = "Authenticates a user with email and password, returns a JWT and user info.",
            tags = {"Authentication"})
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
                                               @Valid @RequestBody AuthRequest authRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.getEmail(),
                        authRequest.getPassword()
                )
        );

        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();


        return ResponseEntity.ok(buildAuthResponse(customUserDetails));
    }

    @Operation(summary = "Register a new customer",
            description = "Registers a new customer and returns a JWT and user info.",
            tags = {"Authentication"})
    @PostMapping("/register/customer")
    public ResponseEntity<AuthResponse> registerCustomer(
            @Valid @RequestBody CustomerRequest customerRequest) {

            customerService.saveCustomer(customerRequest);

            CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(customerRequest.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(buildAuthResponse(customUserDetails));

    }

    @Operation(summary = "Register a new store admin",
            description = "Registers a new store admin and returns a JWT and user info. Requires a valid invitation code.",
            tags = {"Authentication"})
    @PostMapping("/register/store-admin")
    public ResponseEntity<AuthResponse> registerStoreAdmin(
            @Valid @RequestBody StoreAdminRequest storeAdminRequest) {

            invitationCodeService.validateStoreAdminInvitationCode(
                    storeAdminRequest.getInvitationCode(),
                    storeAdminRequest.getEmail()
            );

            storeAdminService.saveStoreAdmin(storeAdminRequest);


            CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(storeAdminRequest.getEmail());
            UUID createdUserEntityId = customUserDetails.getGenericUserId();


            invitationCodeService.markCodeAsUsed(
                    storeAdminRequest.getInvitationCode(),
                    createdUserEntityId
            );


            return ResponseEntity.status(HttpStatus.CREATED).body(buildAuthResponse(customUserDetails));

    }

    @Operation(summary = "Register a new profeco admin",
            description = "Registers a new profeco admin and returns a JWT and user info. Requires a valid invitation code.",
            tags = {"Authentication"})
    @PostMapping("/register/profeco-admin")
    public ResponseEntity<AuthResponse> registerProfecoAdmin(
            @Valid @RequestBody ProfecoAdminRequest profecoAdminRequest) {

        invitationCodeService.validateProfecoAdminInvitationCode(
                profecoAdminRequest.getInvitationCode(),
                profecoAdminRequest.getEmail()
        );

        profecoAdminService.saveProfecoAdmin(profecoAdminRequest);

        CustomUserDetails customUserDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(profecoAdminRequest.getEmail());
        UUID createdUserEntityId = customUserDetails.getGenericUserId();

        invitationCodeService.markCodeAsUsed(
                profecoAdminRequest.getInvitationCode(),
                createdUserEntityId
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(buildAuthResponse(customUserDetails));
    }

    private AuthResponse buildAuthResponse(CustomUserDetails userDetails) {
        String token = jwtUtil.generateToken(userDetails);
        AuthResponse.AuthResponseBuilder builder = AuthResponse.builder()
                .accessToken(token)
                .id(userDetails.getSpecificUserId())
                .email(userDetails.getUsername())
                .name(userDetails.getSpecificName() != null ? userDetails.getSpecificName() : "")
                .roles(userDetails.getAuthorities().stream()
                        .map(a -> a.getAuthority().replace("ROLE_", ""))
                        .collect(Collectors.toList()));

         if (userDetails.getGenericUserId() != null) {
            builder.userEntityId(userDetails.getGenericUserId());
         }

        return builder.build();
    }
}