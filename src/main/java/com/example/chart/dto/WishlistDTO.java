package com.example.chart.dto;

import lombok.Data;
import java.util.Set;

@Data
public class WishlistDTO {
    private Long id;
    private Long userId;
    private Set<ProductDTO> products;
    private boolean active;
} 