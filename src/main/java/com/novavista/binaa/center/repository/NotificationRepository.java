package com.novavista.binaa.center.repository;

import com.novavista.binaa.center.entity.Notification;
import com.novavista.binaa.center.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUserIdAndArchivedOrderByCreatedAtDesc(Long userId, boolean archived);

    @Query("SELECT n FROM Notification n WHERE n.userId = :userId AND n.status = 'UNREAD' AND n.archived = false")
    List<Notification> findUnreadByUser(@Param("userId") Long userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.status = 'UNREAD' AND n.archived = false")
    long countUnreadByUser(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Notification n SET n.status = :status, n.readAt = CURRENT_TIMESTAMP WHERE n.notificationId IN :ids AND n.userId = :userId")
    int updateNotificationStatus(@Param("ids") List<Long> ids, @Param("status") NotificationStatus status, @Param("userId") Long userId);

    @Modifying
    @Query("UPDATE Notification n SET n.archived = :archived, n.archivedAt = CURRENT_TIMESTAMP WHERE n.notificationId IN :ids AND n.userId = :userId")
    int archiveNotifications(@Param("ids") List<Long> ids, @Param("archived") boolean archived, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM NotificationEntity n WHERE n.id = :id AND n.userId = :userId")
    void deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM NotificationEntity n WHERE n.id IN :ids AND n.userId = :userId")
    void deleteByIdInAndUserId(@Param("ids") List<Long> ids, @Param("userId") Long userId);

    long countByIdInAndUserId(List<Long> ids, Long userId);

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.status = 'READ', n.readAt = CURRENT_TIMESTAMP " +
            "WHERE n.userId = :userId AND n.archived = false AND n.status = 'UNREAD'")
    void markAllAsRead(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE NotificationEntity n SET n.archived = true, n.archivedAt = CURRENT_TIMESTAMP " +
            "WHERE n.userId = :userId AND n.archived = false")
    void archiveAllByUser(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM NotificationEntity n WHERE n.userId = :userId")
    void deleteAllByUser(@Param("userId") Long userId);

    @Modifying
    @Query("DELETE FROM NotificationEntity n WHERE n.userId = :userId AND n.archived = true")
    void deleteAllArchivedByUser(@Param("userId") Long userId);
}