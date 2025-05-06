package com.example.chart.dto.products;

import lombok.Data;

@Data
public class ProductSearchDTO {
    private String keyword;
    private String sortBy;
    private String sortDirection;
    private int page;
    private int size;
} 