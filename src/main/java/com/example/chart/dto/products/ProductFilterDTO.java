package com.example.chart.dto.products;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

@Data
public class ProductFilterDTO {
    private String keyword;
    private Long categoryId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private String brand;
    private Double minRating;
    private String sortBy;
    private String sortDirection;
}
