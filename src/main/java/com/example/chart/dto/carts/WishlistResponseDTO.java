package com.example.chart.dto.carts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistResponseDTO {
    private Long id;
    private Long userId;
    private List<WishlistItemResponseDTO> items;
}
