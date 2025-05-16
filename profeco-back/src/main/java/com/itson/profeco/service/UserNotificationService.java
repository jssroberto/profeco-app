package com.itson.profeco.service;

import com.itson.profeco.api.dto.response.NotificationResponse;
import com.itson.profeco.api.dto.response.UnreadNotificationsCountResponse;
import com.itson.profeco.mapper.UserNotificationMapper;
import com.itson.profeco.model.UserEntity;
import com.itson.profeco.model.UserNotification;
import com.itson.profeco.repository.UserNotificationRepository;
import com.itson.profeco.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserNotificationService {

    private final UserNotificationRepository userNotificationRepository;
    private final UserRepository userRepository;
    private final UserNotificationMapper userNotificationMapper;

    @Transactional
    public void createNotification(UUID userId, String message, String type, String link) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        UserNotification notification = new UserNotification(user, message, type, link);
        userNotificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getNotificationsForUser(UUID userId, Pageable pageable) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        Page<UserNotification> userNotificationsPage =
                userNotificationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        return userNotificationsPage.map(userNotificationMapper::toResponse);
    }

    @Transactional(readOnly = true)
    public UnreadNotificationsCountResponse getUnreadNotificationsCount(UUID userId) { // Acepta userId
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        long count = userNotificationRepository.countByUserAndIsReadFalse(user);
        return new UnreadNotificationsCountResponse(count);
    }

    @Transactional
    public NotificationResponse markAsRead(UUID notificationId, UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        UserNotification notification = userNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found: " + notificationId));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new SecurityException("User not authorized to mark this notification as read.");
        }

        if (!notification.isRead()) {
            notification.setRead(true);
            userNotificationRepository.save(notification);
        }
        return userNotificationMapper.toResponse(notification);
    }

    @Transactional
    public void markAllAsRead(UUID userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        userNotificationRepository.markAllAsReadForUser(user);
    }
}