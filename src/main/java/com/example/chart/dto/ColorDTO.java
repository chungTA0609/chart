package com.example.chart.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ColorDTO {
    private Long id;

    @NotBlank(message = "Color name is required")
    private String name;

    @NotBlank(message = "Color value is required")
    private String value;

    @NotBlank(message = "Color hex is required")
    private String hex;

    private Long productId;
} 