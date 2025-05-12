package com.itson.profeco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.itson.profeco.api.dto.response.CustomerResponse;
import com.itson.profeco.api.dto.response.ProfecoAdminResponse;
import com.itson.profeco.api.dto.response.StoreAdminResponse;
import com.itson.profeco.mapper.AuthMapper;
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

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for authentication and registration")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final CustomerService userService;
    private final StoreAdminService storeAdminService;
    private final ProfecoAdminService profecoAdminService;
    private final AuthMapper authMapper;
    private final InvitationCodeService invitationCodeService;

    @Operation(summary = "Authenticate user and return JWT",
            description = "Authenticates a user with email and password, returns a JWT and user info.",
            tags = {"Authentication"})
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> createAuthenticationToken(
            @Valid @RequestBody AuthRequest authRequest) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(), authRequest.getPassword()));

        final UserDetails userDetails =
                userDetailsService.loadUserByUsername(authRequest.getEmail());

        final String jwt = jwtUtil.generateToken(userDetails);

        CustomerResponse customerResponse =
                userService.getCustomerByEmail(userDetails.getUsername());

        AuthResponse authResponse = authMapper.fromCustomerResponse(customerResponse);
        authResponse.setAccessToken(jwt);

        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Register a new customer",
            description = "Registers a new customer and returns a JWT and user info.",
            tags = {"Authentication"})
    @PostMapping("/register/customer")
    public ResponseEntity<AuthResponse> registerCustomer(
            @Valid @RequestBody CustomerRequest customerRequest) {
        CustomerResponse customerResponse = userService.saveCustomer(customerRequest);

        final UserDetails userDetails =
                userDetailsService.loadUserByUsername(customerRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        AuthResponse authResponse = authMapper.fromCustomerResponse(customerResponse);
        authResponse.setAccessToken(jwt);

        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Register a new store admin",
            description = "Registers a new store admin and returns a JWT and user info. Requires a valid invitation code.",
            tags = {"Authentication"})
    @PostMapping("/register/store-admin")
    public ResponseEntity<AuthResponse> registerStoreAdmin(
            @Valid @RequestBody StoreAdminRequest storeAdminRequest) {
        invitationCodeService.validateInvitationCode(storeAdminRequest);
        StoreAdminResponse storeAdminResponse = storeAdminService.saveStoreAdmin(storeAdminRequest);
        invitationCodeService.markCodeAsUsed(storeAdminRequest.getInvitationCode(),
                storeAdminResponse.getUserId());

        final UserDetails userDetails =
                userDetailsService.loadUserByUsername(storeAdminRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        AuthResponse authResponse = authMapper.fromStoreAdminResponse(storeAdminResponse);
        authResponse.setAccessToken(jwt);

        return ResponseEntity.ok(authResponse);
    }

    @Operation(summary = "Register a new profeco admin",
            description = "Registers a new profeco admin and returns a JWT and user info. Requires a valid invitation code.",
            tags = {"Authentication"})
    @PostMapping("/register/profeco-admin")
    public ResponseEntity<AuthResponse> registerProfecoAdmin(
            @Valid @RequestBody ProfecoAdminRequest profecoAdminRequest) {
        invitationCodeService.validateInvitationCode(profecoAdminRequest);
        ProfecoAdminResponse profecoAdminResponse =
                profecoAdminService.saveProfecoAdmin(profecoAdminRequest);
        invitationCodeService.markCodeAsUsed(profecoAdminRequest.getInvitationCode(),
                profecoAdminResponse.getUserId());

        final UserDetails userDetails =
                userDetailsService.loadUserByUsername(profecoAdminRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        AuthResponse authResponse = authMapper.fromProfecoAdminResponse(profecoAdminResponse);
        authResponse.setAccessToken(jwt);

        return ResponseEntity.ok(authResponse);
    }

}
