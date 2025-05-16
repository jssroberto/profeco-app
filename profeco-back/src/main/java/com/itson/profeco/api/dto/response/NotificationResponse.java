package com.itson.profeco.api.dto.response;


import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private UUID id;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
    private String type;
    private String link;
}