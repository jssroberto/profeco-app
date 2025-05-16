package com.itson.profeco.controller;

import com.itson.profeco.api.dto.response.NotificationResponse;
import com.itson.profeco.api.dto.response.UnreadNotificationsCountResponse;
import com.itson.profeco.model.UserEntity;
import com.itson.profeco.security.CustomUserDetails;
import com.itson.profeco.service.UserNotificationService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.itson.profeco.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/me/notifications")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole(@environment.getProperty('role.customer'))")
public class UserNotificationController {

    private final UserNotificationService userNotificationService;
    private final UserRepository userRepository;

    private UserEntity getAuthenticatedUserEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new IllegalStateException("No authenticated user or user is anonymous.");
        }

        if (!(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new IllegalStateException("Unexpected authentication principal type. Expected CustomUserDetails.");
        }

        UUID userId = ((CustomUserDetails) authentication.getPrincipal()).getGenericUserId();
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found in database with ID: " + userId));
    }

    @Operation(summary = "Get all my notifications", description = "Returns all notifications for the currently authenticated customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notifications retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role")
    })
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getAllMyNotifications() {
        UUID userId = getAuthenticatedUserEntity().getId();
        List<NotificationResponse> notifications = userNotificationService.getAllNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }

    @Operation(summary = "Get my active notifications", description = "Returns only active notifications for the authenticated customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Active notifications retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role")
    })
    @GetMapping("/active")
    public ResponseEntity<List<NotificationResponse>> getMyActiveNotifications() {
        UUID userId = getAuthenticatedUserEntity().getId();
        List<NotificationResponse> activeNotifications = userNotificationService.getActiveNotificationsForUser(userId);
        return ResponseEntity.ok(activeNotifications);
    }

    @Operation(summary = "Get count of unread notifications", description = "Returns the number of unread notifications for the authenticated customer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unread count retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role")
    })
    @GetMapping("/unread-count")
    public ResponseEntity<UnreadNotificationsCountResponse> getMyUnreadNotificationsCount() {
        UUID userId = getAuthenticatedUserEntity().getId();
        UnreadNotificationsCountResponse countResponse = userNotificationService.getUnreadNotificationsCount(userId);
        return ResponseEntity.ok(countResponse);
    }

    @Operation(summary = "Mark a specific notification as read", description = "Marks a specific notification as read if it belongs to the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification marked as read"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role or does not own the notification"),
            @ApiResponse(responseCode = "404", description = "Notification not found")
    })
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markNotificationAsRead(
            @Parameter(description = "ID of the notification to mark as read", required = true)
            @PathVariable UUID notificationId) {
        UUID userId = getAuthenticatedUserEntity().getId();
        NotificationResponse updatedNotification = userNotificationService.markAsRead(notificationId, userId);
        return ResponseEntity.ok(updatedNotification);
    }

    @Operation(summary = "Mark all notifications as read", description = "Marks all notifications as read for the authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "All notifications marked as read successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - User does not have required role")
    })
    @PutMapping("/mark-all-read")
    public ResponseEntity<Void> markAllMyNotificationsAsRead() {
        UUID userId = getAuthenticatedUserEntity().getId();
        userNotificationService.markAllAsRead(userId);
        return ResponseEntity.noContent().build();
    }
}
