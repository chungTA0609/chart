package com.example.chart.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class CheckoutResponseDTO {
    private Long userId;
    private Set<OrderItemDTO> items;
    private AddressDTO selectedAddress;
    private ShippingMethodDTO shippingMethod;
    private PaymentMethodDTO paymentMethod;
    private String promoCode;
    private BigDecimal subtotal;
    private BigDecimal discount;
    private BigDecimal totalAmount;
} 