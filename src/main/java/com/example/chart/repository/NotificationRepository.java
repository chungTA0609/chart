package com.example.chart.repository;

import com.example.chart.models.Notification;
import com.example.chart.models.NotificationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByIdAndActiveTrue(Long id);
    List<Notification> findByUserIdAndActiveTrue(Long userId);
    Page<Notification> findByUserIdAndActiveTrue(Long userId, Pageable pageable);
    List<Notification> findByUserIdAndReadFalseAndActiveTrue(Long userId);
    List<Notification> findByUserIdAndTypeAndActiveTrue(Long userId, NotificationType type);
    long countByUserIdAndReadFalseAndActiveTrue(Long userId);
} 