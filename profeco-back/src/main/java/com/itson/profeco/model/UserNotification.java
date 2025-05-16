package com.itson.profeco.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_notifications")
@Getter
@Setter
@NoArgsConstructor
public class UserNotification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user; // El usuario al que va dirigida la notificación

    @Column(nullable = false, length = 500)
    private String message;

    @Column(name = "is_read", nullable = false)
    private boolean isRead = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(length = 100)
    private String type; // Ej. "NUEVA_OFERTA", "MENSAJE_ADMIN"

    @Column(length = 500)
    private String link; // Opcional: URL a la que dirigir si se hace clic

    public UserNotification(UserEntity user, String message, String type, String link) {
        this.user = user;
        this.message = message;
        this.type = type;
        this.link = link;
        this.isRead = false; // Por defecto no leída
    }
}