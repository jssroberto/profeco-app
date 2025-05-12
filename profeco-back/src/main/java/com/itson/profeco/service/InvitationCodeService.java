package com.itson.profeco.service;

import java.time.LocalDateTime;
import java.util.UUID; // Added import
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.request.ProfecoAdminRequest;
import com.itson.profeco.api.dto.request.StoreAdminRequest;
import com.itson.profeco.api.dto.response.InvitationCodeResponse;
import com.itson.profeco.model.InvitationCode;
import com.itson.profeco.model.UserEntity; // Added import
import com.itson.profeco.repository.InvitationCodeRepository;
import com.itson.profeco.repository.UserRepository; // Added import
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvitationCodeService {

    private final InvitationCodeRepository invitationCodeRepository;
    private final UserRepository userRepository; // Added field
    private static final String PROFECO_ADMIN_ROLE = "PROFECO_ADMIN";
    private static final String STORE_ADMIN_ROLE = "STORE_ADMIN";

    @Transactional(readOnly = true)
    public InvitationCodeResponse checkAndGetInvitationRole(String code) {
        InvitationCode invitationCode = invitationCodeRepository.findByCode(code).orElseThrow(
                () -> new IllegalArgumentException("Invalid invitation code: " + code));

        if (invitationCode.isUsed()) {
            throw new IllegalArgumentException("Invitation code has already been used: " + code);
        }

        if (invitationCode.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invitation code has expired: " + code);
        }

        return new InvitationCodeResponse(invitationCode.getCode(),
                invitationCode.getRole().getName(), invitationCode.getRole().getId());
    }

    @Transactional(readOnly = true)
    public Boolean validateInvitationCode(ProfecoAdminRequest profecoAdminRequest) {
        return validateInvitationCode(profecoAdminRequest.getInvitationCode(), PROFECO_ADMIN_ROLE);
    }

    @Transactional(readOnly = true)
    public Boolean validateInvitationCode(StoreAdminRequest storeAdminRequest) {
        return validateInvitationCode(storeAdminRequest.getInvitationCode(), STORE_ADMIN_ROLE);
    }


    @Transactional(readOnly = true)
    private Boolean validateInvitationCode(String code, String expectedRoleName) {
        InvitationCode invitationCode = invitationCodeRepository.findByCode(code).orElseThrow(
                () -> new IllegalArgumentException("Invalid invitation code: " + code));

        if (invitationCode.isUsed()) {
            throw new IllegalArgumentException("Invitation code has already been used: " + code);
        }

        if (invitationCode.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invitation code has expired: " + code);
        }

        if (!invitationCode.getRole().getName().equalsIgnoreCase(expectedRoleName)) {
            throw new IllegalArgumentException(
                    "Invitation code is for role '" + invitationCode.getRole().getName()
                            + "', but action is for role '" + expectedRoleName + "'.");
        }

        return true;
    }

    @Transactional
    public void markCodeAsUsed(String code, UUID userId) {
        InvitationCode invitationCode = invitationCodeRepository.findByCode(code).orElseThrow(
                () -> new IllegalArgumentException("Invalid invitation code: " + code));

        if (invitationCode.isUsed()) {
            throw new IllegalStateException(
                    "Invitation code " + code + " is already marked as used.");
        }

        if (invitationCode.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Invitation code " + code + " has expired.");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId
                        + ". Cannot mark invitation code as used."));

        invitationCode.setUsed(true);
        invitationCode.setUsedBy(user);
        invitationCodeRepository.save(invitationCode);
    }
}

