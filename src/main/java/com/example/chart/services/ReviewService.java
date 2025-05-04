package com.example.chart.services;

import com.example.chart.dto.ReviewDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    ReviewDTO createReview(Long userId, Long orderId, ReviewDTO reviewDTO);
    ReviewDTO getReviewById(Long id);
    Page<ReviewDTO> getProductReviews(Long productId, Pageable pageable);
    Page<ReviewDTO> getUserReviews(Long userId, Pageable pageable);
    ReviewDTO updateReview(Long id, ReviewDTO reviewDTO);
    void deleteReview(Long id);
    boolean hasUserReviewedProduct(Long userId, Long productId);
    double getProductAverageRating(Long productId);
} 