package com.novavista.binaa.center.listener;

import com.novavista.binaa.center.dto.request.NotificationDTO;
import com.novavista.binaa.center.event.NotificationEvent;
import com.novavista.binaa.center.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class NotificationEventListener {
    private final NotificationService notificationService;

    @Autowired
    public NotificationEventListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Async
    @EventListener
    @Transactional
    public void handleNotificationEvent(NotificationEvent event) {
        log.debug("Handling notification event: {}", event);
        try {
            NotificationDTO notification = notificationService.createNotification(event.toNotificationRequest());
            log.debug("Created notification with ID: {}", notification.getNotificationId());
        } catch (Exception e) {
            log.error("Error processing notification event", e);
        }
    }
}