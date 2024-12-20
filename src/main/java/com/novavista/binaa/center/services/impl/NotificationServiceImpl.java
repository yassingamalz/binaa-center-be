package com.novavista.binaa.center.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novavista.binaa.center.dto.request.NotificationActionRequest;
import com.novavista.binaa.center.dto.request.NotificationDTO;
import com.novavista.binaa.center.dto.request.NotificationRequest;
import com.novavista.binaa.center.entity.Notification;
import com.novavista.binaa.center.enums.NotificationStatus;
import com.novavista.binaa.center.exceptions.ResourceNotFoundException;
import com.novavista.binaa.center.mapper.NotificationMapper;
import com.novavista.binaa.center.repository.NotificationRepository;
import com.novavista.binaa.center.services.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional
@Slf4j

public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final ObjectMapper objectMapper;


    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   NotificationMapper notificationMapper,
                                   ObjectMapper objectMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
        this.objectMapper = objectMapper;
    }
    @Override
    public NotificationDTO getById(Long id) {
        Notification entity = notificationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + id));
        return notificationMapper.toDto(entity);
    }

    @Override
    public List<NotificationDTO> getUserNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndArchivedOrderByCreatedAtDesc(userId, false);
        return notificationMapper.toDtoList(notifications);
    }

    @Override
    public List<NotificationDTO> getArchivedNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndArchivedOrderByCreatedAtDesc(userId, true);
        return notificationMapper.toDtoList(notifications);
    }

    @Override
    public NotificationDTO createNotification(NotificationRequest request) {
        Notification entity = Notification.builder()
                .userId(Objects.requireNonNullElse(request.getUserId(),1L))
                .type(request.getType())
                .title(request.getTitle())
                .message(request.getMessage())
                .status(NotificationStatus.UNREAD)
                .data(convertMapToString(request.getData()))
                .createdAt(LocalDateTime.now())
                .archived(false)
                .build();

        Notification savedEntity = notificationRepository.save(entity);
        return notificationMapper.toDto(savedEntity);
    }

    @Override
    public long getUnreadCount(Long userId) {
        return notificationRepository.countUnreadByUser(userId);
    }

    @Override
    @Transactional
    public void performAction(Long userId, NotificationActionRequest request) {
        validateNotificationIds(userId, request.getNotificationIds());

        switch (request.getAction()) {
            case MARK_READ:
                notificationRepository.updateNotificationStatus(
                        request.getNotificationIds(),
                        NotificationStatus.READ,
                        userId
                );
                break;

            case MARK_UNREAD:
                notificationRepository.updateNotificationStatus(
                        request.getNotificationIds(),
                        NotificationStatus.UNREAD,
                        userId
                );
                break;

            case ARCHIVE:
                notificationRepository.archiveNotifications(
                        request.getNotificationIds(),
                        true,
                        userId
                );
                break;

            case UNARCHIVE:
                notificationRepository.archiveNotifications(
                        request.getNotificationIds(),
                        false,
                        userId
                );
                break;

            case DELETE:
                notificationRepository.deleteByNotificationIdInAndUserId(
                        request.getNotificationIds(),
                        userId
                );
                break;

            default:
                throw new IllegalArgumentException("Unsupported notification action: " + request.getAction());
        }
    }

    @Override
    @Transactional
    public void deleteNotification(Long userId, Long notificationId) {
        validateNotificationIds(userId, List.of(notificationId));
        notificationRepository.deleteByNotificationIdAndUserId(notificationId, userId);
    }

    private void validateNotificationIds(Long userId, List<Long> notificationIds) {
        long count = notificationRepository.countByNotificationIdInAndUserId(notificationIds, userId);
        if (count != notificationIds.size()) {
            throw new ResourceNotFoundException("One or more notifications not found or don't belong to the user");
        }
    }

    @Override
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }

    @Override
    public void archiveAll(Long userId) {
        notificationRepository.archiveAllByUser(userId);
    }

    @Override
    public void deleteAll(Long userId) {
        notificationRepository.deleteAllByUser(userId);
    }

    @Override
    public void deleteAllArchived(Long userId) {
        notificationRepository.deleteAllArchivedByUser(userId);
    }

    private String convertMapToString(Map<String, Object> data) {
        try {
            return data != null ? objectMapper.writeValueAsString(data) : null;
        } catch (JsonProcessingException e) {
            log.error("Error converting map to JSON string", e);
            return null;
        }
    }

}