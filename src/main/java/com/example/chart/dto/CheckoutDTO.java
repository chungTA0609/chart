package com.example.chart.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class CheckoutDTO {
    private Long userId;
    private Set<OrderItemDTO> items;
    private Long shippingMethodId;
    private Long paymentMethodId;
    private String promoCode;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal totalAmount;
    private Long selectedAddressId;
} 