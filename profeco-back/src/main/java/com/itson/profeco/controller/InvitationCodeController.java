package com.itson.profeco.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.itson.profeco.api.dto.response.InvitationCodeResponse;
import com.itson.profeco.service.InvitationCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/invitation-codes")
@RequiredArgsConstructor
@Tag(name = "Invitation Codes", description = "Endpoints for managing invitation codes")
public class InvitationCodeController {

    private final InvitationCodeService invitationCodeService;

    @Operation(summary = "Check invitation code and get role",
            description = "Checks if an invitation code is valid and returns the associated role.",
            tags = {"Invitation Codes"})
    @GetMapping("/check/{code}")
    public ResponseEntity<InvitationCodeResponse> checkInvitationCode(@PathVariable String code) {
        InvitationCodeResponse response = invitationCodeService.checkAndGetInvitationRole(code);
        return ResponseEntity.ok(response);
    }
}
