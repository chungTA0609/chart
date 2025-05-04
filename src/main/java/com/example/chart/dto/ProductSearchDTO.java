package com.example.chart.dto;

import lombok.Data;

@Data
public class ProductSearchDTO {
    private String keyword = "";
    private String sortBy = "id";
    private String sortDirection = "asc";
    private int page = 1;
    private int size = 10;
} 