package com.example.chart.dto;

import lombok.Data;

@Data
public class ShippingMethodDTO {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer estimatedDays;
    private boolean isActive;
} 