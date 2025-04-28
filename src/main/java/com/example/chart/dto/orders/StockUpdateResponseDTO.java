package com.example.chart.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockUpdateResponseDTO {
    private boolean success;
    private String message;
}
