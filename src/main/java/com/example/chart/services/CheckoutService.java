package com.example.chart.services;

import com.example.chart.dto.CheckoutDTO;
import com.example.chart.dto.OrderDTO;
import com.example.chart.dto.orders.OrderItemDTO;
import com.example.chart.dto.orders.OrderSummaryDTO;
import com.example.chart.models.*;
import com.example.chart.repository.AddressRepository;
import com.example.chart.repository.OrderRepository;
import com.example.chart.repository.PromoCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface CheckoutService {

    CheckoutDTO initializeCheckout(Long userId);
    CheckoutDTO applyPromoCode(Long userId, String promoCode);
    CheckoutDTO updateShippingAddress(Long userId, Long shippingAddressId, Long billingAddressId);
    CheckoutDTO updateShippingMethod(Long userId, ShippingMethod shippingMethod);
    CheckoutDTO removePromoCode(Long userId);
    CheckoutDTO calculateTotals(Long userId);
    OrderDTO processPayment(Long userId, String paymentMethod);
    boolean validateCheckout(Long userId);
}
