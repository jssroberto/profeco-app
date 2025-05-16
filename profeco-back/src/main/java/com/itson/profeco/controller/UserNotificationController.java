package com.itson.profeco.controller;

import com.itson.profeco.api.dto.response.NotificationResponse;
import com.itson.profeco.api.dto.response.UnreadNotificationsCountResponse;
import com.itson.profeco.model.UserEntity;
import com.itson.profeco.security.CustomUserDetails;
import com.itson.profeco.service.UserNotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RestController;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.itson.profeco.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/me/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UserNotificationController {

    private final UserNotificationService userNotificationService;
    private final UserRepository userRepository;

    private UserEntity getAuthenticatedUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("No hay un usuario autenticado o es anónimo.");
        }

        Object principal = authentication.getPrincipal();
        UUID userId;

        if (principal instanceof CustomUserDetails) {
            userId = ((CustomUserDetails) principal).getGenericUserId();
        } else if (principal instanceof String) {
            throw new IllegalStateException("El principal de autenticación no es del tipo esperado (CustomUserDetails).");
        } else {
            throw new IllegalStateException("Tipo de principal de autenticación no soportado: " + principal.getClass().getName());
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
    }

    @GetMapping
    public ResponseEntity<Page<NotificationResponse>> getMyNotifications(
            @PageableDefault(size = 10, sort = "createdAt,desc") Pageable pageable) {
        UserEntity currentUser = getAuthenticatedUserEntity();
        Page<NotificationResponse> notifications = userNotificationService.getNotificationsForUser(currentUser.getId(), pageable); // Pasamos solo el ID
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread-count")
    public ResponseEntity<UnreadNotificationsCountResponse> getMyUnreadNotificationsCount() {
        UserEntity currentUser = getAuthenticatedUserEntity();
        UnreadNotificationsCountResponse countResponse = userNotificationService.getUnreadNotificationsCount(currentUser.getId()); // Pasamos solo el ID
        return ResponseEntity.ok(countResponse);
    }

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markNotificationAsRead(@PathVariable UUID notificationId) {
        UserEntity currentUser = getAuthenticatedUserEntity();
        NotificationResponse notificationResponse = userNotificationService.markAsRead(notificationId, currentUser.getId()); // Pasamos solo el ID
        return ResponseEntity.ok(notificationResponse);
    }

    @PutMapping("/mark-all-read")
    public ResponseEntity<Void> markAllMyNotificationsAsRead() {
        UserEntity currentUser = getAuthenticatedUserEntity();
        userNotificationService.markAllAsRead(currentUser.getId());
        return ResponseEntity.noContent().build();
    }
}