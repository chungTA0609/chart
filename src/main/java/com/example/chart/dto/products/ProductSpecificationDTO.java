package com.example.chart.dto.products;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductSpecificationDTO {
    private Long id;

    @NotBlank(message = "Specification key is required")
    private String key;

    @NotBlank(message = "Specification value is required")
    private String value;

    private Long productId;
} 