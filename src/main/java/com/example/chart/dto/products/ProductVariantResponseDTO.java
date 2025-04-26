package com.example.chart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductVariantResponseDTO {
    private Long id;
    private String size;
    private String color;
    private String otherAttributes;
    private BigDecimal price;
    private Integer stockQuantity;
}
