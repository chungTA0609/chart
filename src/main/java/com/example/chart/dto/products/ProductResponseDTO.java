package com.example.chart.dto.products;


import com.example.chart.dto.carts.CategoryResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private String description;
    private List<String> images;
    private List<CategoryResponseDTO> categories;
    private BigDecimal price;
    private Integer stockQuantity;
    private String brand;
    private List<ProductVariantResponseDTO> variants;
    private Double rating;
    private Integer popularity;
    private LocalDateTime createdDate;
}
