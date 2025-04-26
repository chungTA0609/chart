package com.example.chart.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderSummaryDTO {
    private List<OrderItemDTO> items;
    private BigDecimal subtotal;
    private BigDecimal taxes;
    private BigDecimal shippingFee;
    private BigDecimal discount;
    private BigDecimal total;
}
