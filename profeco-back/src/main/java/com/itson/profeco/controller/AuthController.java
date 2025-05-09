package com.itson.profeco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.api.dto.request.AuthRequest;
import com.itson.profeco.api.dto.request.CustomerRequest;
import com.itson.profeco.api.dto.response.AuthResponse;
import com.itson.profeco.security.JwtUtil;
import com.itson.profeco.service.UserDetailsServiceImpl;
import com.itson.profeco.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final CustomerService userService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> createAuthenticationToken(
            @Valid @RequestBody AuthRequest authRequest) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getEmail(), authRequest.getPassword()));

        final UserDetails userDetails =
                userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerUser(
            @Valid @RequestBody CustomerRequest userRequest) {
        userService.saveCustomer(userRequest);

        // Optionally, you can automatically log in the user after registration
        // and return a JWT token, or just return a success message.
        // For this example, let's generate a token similar to the login endpoint.

        final UserDetails userDetails =
                userDetailsService.loadUserByUsername(userRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}
