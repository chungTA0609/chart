package com.example.chart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantRequestDTO {
    private String size;
    private String color;
    private String otherAttributes;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @NotNull
    @Min(0)
    private Integer stockQuantity;
}
