package com.example.chart.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReviewDTO {
    private Long id;
    private Long userId;
    private String userName;
    private Long productId;
    private String productName;
    private Long orderId;
    private int rating;
    private String comment;
    private LocalDateTime reviewDate;
    private boolean active;
} 