package com.example.chart.dto.carts;

import com.example.chart.dto.orders.CartItemDTO;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class CartDTO {
    private Long id;
    private Long userId;
    private Set<CartItemDTO> items;
    private int totalItems;
    private BigDecimal totalPrice;
    private boolean active;
} 