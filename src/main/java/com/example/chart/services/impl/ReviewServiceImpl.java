package com.example.chart.services.impl;

import com.example.chart.dto.ReviewDTO;
import com.example.chart.models.*;
import com.example.chart.repository.OrderRepository;
import com.example.chart.repository.ProductRepository;
import com.example.chart.repository.ReviewRepository;
import com.example.chart.repository.UserRepository;
import com.example.chart.services.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public ReviewDTO createReview(Long userId, Long orderId, ReviewDTO reviewDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        Product product = productRepository.findById(reviewDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        
        Order order = orderRepository.findByIdAndActiveTrue(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        if (reviewRepository.existsByUserIdAndProductIdAndActiveTrue(userId, product.getId())) {
            throw new IllegalStateException("User has already reviewed this product");
        }

        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setOrder(order);
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setReviewDate(LocalDateTime.now());
        review.setActive(true);

        return mapToDTO(reviewRepository.save(review));
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDTO getReviewById(Long id) {
        Review review = reviewRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        return mapToDTO(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO> getProductReviews(Long productId, Pageable pageable) {
        return reviewRepository.findByProductIdAndActiveTrue(productId, pageable)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ReviewDTO> getUserReviews(Long userId, Pageable pageable) {
        return reviewRepository.findByUserIdAndActiveTrue(userId, pageable)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional
    public ReviewDTO updateReview(Long id, ReviewDTO reviewDTO) {
        Review review = reviewRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));

        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());

        return mapToDTO(reviewRepository.save(review));
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        Review review = reviewRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
        
        review.setActive(false);
        reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasUserReviewedProduct(Long userId, Long productId) {
        return reviewRepository.existsByUserIdAndProductIdAndActiveTrue(userId, productId);
    }

    @Override
    @Transactional(readOnly = true)
    public double getProductAverageRating(Long productId) {
        return reviewRepository.findByProductIdAndActiveTrue(productId).stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);
    }

    private ReviewDTO mapToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setUserId(review.getUser().getId());
        dto.setUserName(review.getUser().getEmail());
        dto.setProductId(review.getProduct().getId());
        dto.setProductName(review.getProduct().getName());
        dto.setOrderId(review.getOrder().getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setReviewDate(review.getReviewDate());
        dto.setActive(review.isActive());
        return dto;
    }
} 