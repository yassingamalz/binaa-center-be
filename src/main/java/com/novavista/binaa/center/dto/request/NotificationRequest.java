package com.novavista.binaa.center.dto.request;

import com.novavista.binaa.center.enums.NotificationType;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class NotificationRequest {
    private Long userId;
    private NotificationType type;
    private String title;
    private String message;
    private Map<String, Object> data;
}