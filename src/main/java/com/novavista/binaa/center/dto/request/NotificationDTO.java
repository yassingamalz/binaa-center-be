package com.novavista.binaa.center.dto.request;

import com.novavista.binaa.center.enums.NotificationStatus;
import com.novavista.binaa.center.enums.NotificationType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long notificationId;
    private Long userId;
    private NotificationType type;
    private String title;
    private String message;
    private NotificationStatus status;
    private Map<String, Object> data;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private boolean archived;
    private LocalDateTime archivedAt;
}
