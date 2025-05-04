package com.example.chart.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class CheckoutDTO {
    private Long userId;
    private Set<OrderItemDTO> items;
    private String shippingAddress;
    private String billingAddress;
    private String shippingMethod;
    private String paymentMethod;
    private String promoCode;
    private BigDecimal subtotal;
    private BigDecimal shippingCost;
    private BigDecimal discount;
    private BigDecimal totalAmount;
    private boolean useExistingAddress;
    private Long selectedAddressId;
} 