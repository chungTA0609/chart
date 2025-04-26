package com.example.chart.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistItemResponseDTO {
    private Long id;
    private Long productId;
    private String productName;
}
