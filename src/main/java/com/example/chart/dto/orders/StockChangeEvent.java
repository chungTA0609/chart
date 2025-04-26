package com.example.chart.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockChangeEvent {
    private Long productId;
    private Integer quantityChanged;
    private Integer newStockQuantity;
    private String eventType; // e.g., "DECREMENT", "INCREMENT"
    private String timestamp;

}
