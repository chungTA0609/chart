package com.example.chart.repository;

import com.example.chart.models.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findByIdAndActiveTrue(Long id);
    List<Review> findByProductIdAndActiveTrue(Long productId);
    Page<Review> findByProductIdAndActiveTrue(Long productId, Pageable pageable);
    List<Review> findByUserIdAndActiveTrue(Long userId);
    Page<Review> findByUserIdAndActiveTrue(Long userId, Pageable pageable);
    List<Review> findByOrderIdAndActiveTrue(Long orderId);
    boolean existsByUserIdAndProductIdAndActiveTrue(Long userId, Long productId);
} 