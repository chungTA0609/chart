package com.example.chart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductDimensionDTO {
    private Long id;

    @NotBlank(message = "Dimension key is required")
    private String key;

    @NotBlank(message = "Dimension value is required")
    private String value;

    private Long productId;
} 