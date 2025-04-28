package com.example.chart.dto.products;

import com.example.chart.models.Dimensions;
import com.example.chart.models.Specifications;
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
    private Long categoryIds;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @NotNull
    @Min(0)
    private Integer stockQuantity;
    private String brand;
    private List<ProductVariantRequestDTO> variants;
    private Double rating;
    private List<String> colors;
    private List<String> sizes;
    private List<Specifications> specifications;
    private List<Dimensions> dimensions;
}
