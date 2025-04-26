package com.example.chart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {
    @NotBlank
    private String name;
    private String description;
    private List<String> images;
    private List<Long> categoryIds;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @NotNull
    @Min(0)
    private Integer stockQuantity;
    private String brand;
    private List<ProductVariantRequestDTO> variants;
    private Double rating;
}
