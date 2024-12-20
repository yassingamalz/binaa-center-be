package com.novavista.binaa.center.services.impl;

import com.novavista.binaa.center.event.NotificationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public NotificationEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishNotificationEvent(NotificationEvent event) {
        log.debug("Publishing notification event: {}", event);
        eventPublisher.publishEvent(event);
    }
}