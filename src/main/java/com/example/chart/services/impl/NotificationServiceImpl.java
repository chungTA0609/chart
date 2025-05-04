package com.example.chart.services.impl;

import com.example.chart.dto.NotificationDTO;
import com.example.chart.models.*;
import com.example.chart.repository.NotificationRepository;
import com.example.chart.repository.UserRepository;
import com.example.chart.services.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public NotificationDTO createNotification(Long userId, NotificationDTO notificationDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(notificationDTO.getTitle());
        notification.setMessage(notificationDTO.getMessage());
        notification.setType(notificationDTO.getType());
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setActive(true);

        return mapToDTO(notificationRepository.save(notification));
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationDTO getNotificationById(Long id) {
        Notification notification = notificationRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));
        return mapToDTO(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<NotificationDTO> getUserNotifications(Long userId, Pageable pageable) {
        return notificationRepository.findByUserIdAndActiveTrue(userId, pageable)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDTO> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserIdAndReadFalseAndActiveTrue(userId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Long id) {
        Notification notification = notificationRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification not found"));

        notification.setRead(true);
        notification.setReadAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndReadFalseAndActiveTrue(userId);
        LocalDateTime now = LocalDateTime.now();
        
        notifications.forEach(notification -> {
            notification.setRead(true);
            notification.setReadAt(now);
        });
        
        notificationRepository.saveAll(notifications);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(Long userId) {
        return notificationRepository.countByUserIdAndReadFalseAndActiveTrue(userId);
    }

    @Override
    @Transactional
    public void createOrderNotification(Long userId, Long orderId, NotificationType type, String message) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setTitle("Order Update");
        notificationDTO.setMessage(message);
        notificationDTO.setType(type);
        
        createNotification(userId, notificationDTO);
    }

    @Override
    @Transactional
    public void createProductNotification(Long userId, Long productId, NotificationType type, String message) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setTitle("Product Update");
        notificationDTO.setMessage(message);
        notificationDTO.setType(type);
        
        createNotification(userId, notificationDTO);
    }

    private NotificationDTO mapToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        dto.setId(notification.getId());
        dto.setUserId(notification.getUser().getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setType(notification.getType());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());
        dto.setReadAt(notification.getReadAt());
        dto.setActive(notification.isActive());
        return dto;
    }
} 