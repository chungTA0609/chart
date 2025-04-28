package com.example.chart.models;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Products product;

    private String size;
    private String color;
    private String otherAttributes;

    private BigDecimal price;
    private Integer stockQuantity;

    @Version
    private Long version; // For optimistic locking

    // Getters and Setters
}
