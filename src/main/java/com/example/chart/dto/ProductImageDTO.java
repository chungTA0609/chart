package com.example.chart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductImageDTO {
    private Long id;
    
    @NotBlank(message = "Image URL is required")
    private String imageUrl;
    
    private boolean isMain;
} 