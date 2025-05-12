package com.itson.profeco.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.response.InvitationCodeResponse;
import com.itson.profeco.model.InvitationCode;
import com.itson.profeco.repository.InvitationCodeRepository;
import lombok.RequiredArgsConstructor;
import com.itson.profeco.exceptions.InvalidInvitationCodeException;
import com.itson.profeco.model.UserEntity;
import com.itson.profeco.repository.UserRepository;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvitationCodeService {

    private final InvitationCodeRepository invitationCodeRepository;
    private final UserRepository userRepository;

    private static final String PROFECO_ADMIN_ROLE_NAME = "PROFECO_ADMIN";
    private static final String STORE_ADMIN_ROLE_NAME = "STORE_ADMIN";

    @Transactional(readOnly = true)
    public InvitationCodeResponse checkAndGetInvitationRole(String code) {
        InvitationCode invitationCode = invitationCodeRepository.findByCode(code)
                .orElseThrow(() -> new InvalidInvitationCodeException("Invalid invitation code: " + code));

        if (invitationCode.isUsed()) {
            throw new InvalidInvitationCodeException("The invitation code has already been used: " + code);
        }

        if (invitationCode.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new InvalidInvitationCodeException("The invitation code has expired: " + code);
        }

        return new InvitationCodeResponse(invitationCode.getCode(),
                invitationCode.getRole().getName(), invitationCode.getRole().getId());
    }

    @Transactional(readOnly = true)
    public void validateProfecoAdminInvitationCode(String code, String email) {
        validateCode(code, PROFECO_ADMIN_ROLE_NAME, email);
    }


    @Transactional(readOnly = true)
    public void validateStoreAdminInvitationCode(String code, String email ) {
        validateCode(code, STORE_ADMIN_ROLE_NAME, email);
    }

    private void validateCode(String code, String expectedRoleName, String email) {
        InvitationCode invitationCode = invitationCodeRepository.findByCode(code)
                .orElseThrow(() -> new InvalidInvitationCodeException("Invalid invitation code: " + code));

        if (invitationCode.isUsed()) {

            throw new InvalidInvitationCodeException("The invitation code has already been used: " + code);
        }

        if (invitationCode.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new InvalidInvitationCodeException("The invitation code has expired: " + code);
        }

        if (!invitationCode.getRole().getName().equalsIgnoreCase(expectedRoleName)) {
            throw new InvalidInvitationCodeException(
                    "The invitation code is for the role '" + invitationCode.getRole().getName()
                            + "', but the role was expected '" + expectedRoleName + "'.");
        }
    }

    @Transactional
    public void markCodeAsUsed(String code, UUID userId) {
        InvitationCode invitationCode = invitationCodeRepository.findByCode(code)
                .orElseThrow(() -> new InvalidInvitationCodeException("Invalid invitation code: " + code + " al intentar marcarlo como usado."));

        if (invitationCode.isUsed()) {
            throw new IllegalStateException(
                    "The invitation code " + code + " is already marked as used.");
        }

        if (invitationCode.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("The invitation code " + code + " has expired and cannot be used.");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId
                        + ". Cannot mark invitation code as used."));

        invitationCode.setUsed(true);
        invitationCode.setUsedBy(user);
        invitationCodeRepository.save(invitationCode);
    }
}