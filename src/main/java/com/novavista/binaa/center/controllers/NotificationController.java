package com.novavista.binaa.center.controllers;

import com.novavista.binaa.center.dto.request.NotificationActionRequest;
import com.novavista.binaa.center.dto.request.NotificationDTO;
import com.novavista.binaa.center.security.CustomUserDetails;
import com.novavista.binaa.center.services.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getUserNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        List<NotificationDTO> notifications = notificationService.getUserNotifications(getUserId(userDetails));
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/archived")
    public ResponseEntity<List<NotificationDTO>> getArchivedNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        List<NotificationDTO> notifications = notificationService.getArchivedNotifications(getUserId(userDetails));
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread/count")
    public ResponseEntity<Long> getUnreadCount(@AuthenticationPrincipal UserDetails userDetails) {
        long count = notificationService.getUnreadCount(getUserId(userDetails));
        return ResponseEntity.ok(count);
    }

    @PostMapping("/action")
    public ResponseEntity<Void> performAction(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody NotificationActionRequest request) {
        notificationService.performAction(getUserId(userDetails), request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(@AuthenticationPrincipal UserDetails userDetails) {
        notificationService.markAllAsRead(getUserId(userDetails));
        return ResponseEntity.ok().build();
    }

    private Long getUserId(UserDetails userDetails) {
        // Implement based on your UserDetails implementation
        return ((CustomUserDetails) userDetails).getUser().getUserId();
    }
}