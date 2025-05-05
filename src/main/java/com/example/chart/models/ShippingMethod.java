package com.example.chart.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "shipping_methods")
public class ShippingMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer estimatedDays;

    @Column(nullable = false)
    private boolean isActive = true;
} 