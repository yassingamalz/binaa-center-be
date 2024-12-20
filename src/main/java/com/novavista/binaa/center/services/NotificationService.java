package com.novavista.binaa.center.services;

import com.novavista.binaa.center.dto.request.NotificationActionRequest;
import com.novavista.binaa.center.dto.request.NotificationDTO;
import com.novavista.binaa.center.dto.request.NotificationRequest;

import java.util.List;

public interface NotificationService {
    NotificationDTO getById(Long id);

    List<NotificationDTO> getUserNotifications(Long userId);

    List<NotificationDTO> getArchivedNotifications(Long userId);

    NotificationDTO createNotification(NotificationRequest request);

    long getUnreadCount(Long userId);

    void performAction(Long userId, NotificationActionRequest request);

    void deleteNotification(Long userId, Long notificationId);

    void markAllAsRead(Long userId);

    void archiveAll(Long userId);

    void deleteAll(Long userId);

    void deleteAllArchived(Long userId);
}