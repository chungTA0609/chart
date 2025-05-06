package com.example.chart.dto.products;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductVariantDTO {
    private Long id;

    @NotBlank(message = "Variant name is required")
    private String name;

    @NotBlank(message = "Variant value is required")
    private String value;

    @NotNull(message = "Stock quantity is required")
    @PositiveOrZero(message = "Stock quantity cannot be negative")
    private Integer stockQuantity;

    private BigDecimal additionalPrice;
} 