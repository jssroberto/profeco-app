package com.itson.profeco.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.itson.profeco.api.dto.response.InvitationCodeResponse; // Added import
import com.itson.profeco.model.InvitationCode;
import com.itson.profeco.repository.InvitationCodeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvitationCodeService {

    private final InvitationCodeRepository invitationCodeRepository;

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
    public Boolean validateInvitationCode(String code, String expectedRoleName) {
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

}

