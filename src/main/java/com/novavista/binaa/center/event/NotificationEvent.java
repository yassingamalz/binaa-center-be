package com.novavista.binaa.center.event;

import com.novavista.binaa.center.dto.request.NotificationRequest;
import com.novavista.binaa.center.enums.NotificationType;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class NotificationEvent {
    private final Long userId;
    private final NotificationType type;
    private final String title;
    private final String message;
    private final Map<String, Object> data;

    public NotificationRequest toNotificationRequest() {
        return NotificationRequest.builder()
                .userId(userId)
                .type(type)
                .title(title)
                .message(message)
                .data(data)
                .build();
    }
}