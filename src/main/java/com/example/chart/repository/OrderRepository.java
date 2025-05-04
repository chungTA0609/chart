package com.example.chart.repository;

import com.example.chart.models.Order;
import com.example.chart.models.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByIdAndActiveTrue(Long id);
    List<Order> findByUserIdAndActiveTrue(Long userId);
    Page<Order> findByUserIdAndActiveTrue(Long userId, Pageable pageable);
    Page<Order> findByStatusAndActiveTrue(OrderStatus status, Pageable pageable);
    Page<Order> findByOrderDateBetweenAndActiveTrue(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
