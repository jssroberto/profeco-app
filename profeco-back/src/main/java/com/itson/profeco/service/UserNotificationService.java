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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserNotificationService {

    private final UserNotificationRepository userNotificationRepository;
    private final UserRepository userRepository;
    private final UserNotificationMapper userNotificationMapper;

    public void createNotification(UUID userId, String message, String type, String link) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        UserNotification notification = new UserNotification();

        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setLink(link);
        notification.setRead(false);

        userNotificationRepository.save(notification);
    }

    public List<NotificationResponse> getAllNotificationsForUser(UUID userId) {
        return userNotificationRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(userNotificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<NotificationResponse> getActiveNotificationsForUser(UUID userId) {
        return userNotificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream()
                .map(userNotificationMapper::toResponse)
                .collect(Collectors.toList());
    }

    public UnreadNotificationsCountResponse getUnreadNotificationsCount(UUID userId) {
        long count = userNotificationRepository.countByUserIdAndIsReadFalse(userId);
        return new UnreadNotificationsCountResponse(count);
    }

    public NotificationResponse markAsRead(UUID notificationId, UUID userId) {
        UserNotification notification = userNotificationRepository.findById(notificationId)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found: " + notificationId));

        if (!notification.getUser().getId().equals(userId)) {
            throw new SecurityException("User not authorized to mark this notification as read.");
        }

        if (!notification.isRead()) {
            notification.setRead(true);
            userNotificationRepository.save(notification);
        }

        return userNotificationMapper.toResponse(notification);
    }

    public void markAllAsRead(UUID userId) {
        userNotificationRepository.markAllAsReadForUser(userId);
    }
}