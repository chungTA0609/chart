package com.example.chart.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID orderId;

    @Column(unique = true)
    private String orderNumber;

    private UUID userId;

    @ElementCollection
    private List<OrderItem> items;

    private BigDecimal subtotal;
    private BigDecimal taxes;
    private BigDecimal shippingFee;
    private BigDecimal discount;
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Embedded
    private EmbeddableAddress shippingAddress;

    private String trackingNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
