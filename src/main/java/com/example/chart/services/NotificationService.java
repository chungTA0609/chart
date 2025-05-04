package com.example.chart.services;

import com.example.chart.dto.NotificationDTO;
import com.example.chart.models.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {
    NotificationDTO createNotification(Long userId, NotificationDTO notificationDTO);
    NotificationDTO getNotificationById(Long id);
    Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable);
    List<NotificationDTO> getUnreadNotifications(Long userId);
    void markAsRead(Long id);
    void markAllAsRead(Long userId);
    long getUnreadCount(Long userId);
    void createOrderNotification(Long userId, Long orderId, NotificationType type, String message);
    void createProductNotification(Long userId, Long productId, NotificationType type, String message);
} 