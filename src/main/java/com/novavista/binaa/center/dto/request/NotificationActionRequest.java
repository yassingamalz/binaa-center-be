package com.novavista.binaa.center.dto.request;

import com.novavista.binaa.center.enums.NotificationAction;
import lombok.Data;

import java.util.List;

@Data
public class NotificationActionRequest {
    private List<Long> notificationIds;
    private NotificationAction action;
}