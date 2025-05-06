package com.example.chart.dto.products;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductImageDTO {
    private Long id;
    
    @NotBlank(message = "Image URL is required")
    private String imageUrl;
    
    private boolean isMain;
    private Long productId;
} 