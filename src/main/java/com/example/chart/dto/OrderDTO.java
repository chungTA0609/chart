package com.example.chart.dto;

import com.example.chart.models.OrderStatus;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class OrderDTO {
    private Long id;
    private Long userId;
    private Set<OrderItemDTO> items;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String billingAddress;
    private String paymentMethod;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private LocalDateTime deliveryDate;
    private String trackingNumber;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 