package com.example.chart.services;

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

@Service
@AllArgsConstructor
public class CheckoutService {

    private OrderRepository orderRepository;

    private AddressRepository addressRepository;

    private PromoCodeRepository promoCodeRepository;

//    @Autowired
//    private RabbitTemplate rabbitTemplate;

    public OrderSummaryDTO getOrderSummary(UUID userId, UUID cartId) {
        // Mock implementation: Fetch from cart service or database
        OrderSummaryDTO summary = new OrderSummaryDTO();
        // Populate items, calculate subtotal, taxes, etc.
        summary.setSubtotal(new BigDecimal("100.00"));
        summary.setTaxes(new BigDecimal("10.00"));
        summary.setShippingFee(new BigDecimal("5.00"));
        summary.setDiscount(new BigDecimal("0.00"));
        summary.setTotal(new BigDecimal("115.00"));
        return summary;
    }

    @Transactional
    public OrderSummaryDTO updateOrderSummary(UUID userId, OrderSummaryDTO updatedSummary) {
        // Validate stock and update summary
        // Recalculate totals
        return updatedSummary;
    }

    public List<Address> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    @Transactional
    public Address saveAddress(Address address) {
        // Validate address fields
        return addressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(Long addressId) {
        addressRepository.deleteById(addressId);
    }

    public List<String> getShippingMethods(UUID addressId) {
        // Mock implementation: Call shipping API
        return List.of("STANDARD", "EXPRESS", "PICKUP");
    }

    @Transactional
    public OrderSummaryDTO applyPromoCode(String code, UUID orderId) {
        PromoCode promo = promoCodeRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid promo code"));

        if (promo.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Promo code expired");
        }

        if (promo.getUsageLimit() != null && promo.getUsedCount() >= promo.getUsageLimit()) {
            throw new RuntimeException("Promo code usage limit reached");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Apply discount logic
        BigDecimal discount = promo.getDiscountType() == DiscountType.PERCENTAGE
                ? order.getSubtotal().multiply(promo.getDiscountValue().divide(new BigDecimal("100")))
                : promo.getDiscountValue();

        order.setDiscount(discount);
        order.setTotal(order.getSubtotal().add(order.getTaxes()).add(order.getShippingFee()).subtract(discount));
        promo.setUsedCount(promo.getUsedCount() + 1);

        orderRepository.save(order);
        promoCodeRepository.save(promo);

        return mapToOrderSummaryDTO(order);
    }

    @Transactional
    public Order placeOrder(Order order, String paymentMethod) {
        // Validate stock, address, payment
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        // Process payment (mock implementation)
        if ("CARD".equals(paymentMethod)) {
            // Integrate with Stripe/PayPal
        } else if ("COD".equals(paymentMethod)) {
            // Validate COD eligibility
        }

        // Send order confirmation to RabbitMQ
//        rabbitTemplate.convertAndSend("order-confirmation-queue", savedOrder.getOrderId().toString());

        savedOrder.setStatus(OrderStatus.CONFIRMED);
        return orderRepository.save(savedOrder);
    }

    private OrderSummaryDTO mapToOrderSummaryDTO(Order order) {
        OrderSummaryDTO dto = new OrderSummaryDTO();
        dto.setItems(order.getItems().stream()
                .map(item -> {
                    OrderItemDTO itemDTO = new OrderItemDTO();
                    itemDTO.setSku(item.getSku());
                    itemDTO.setQuantity(item.getQuantity());
                    itemDTO.setPrice(item.getPrice());
                    return itemDTO;
                })
                .toList());
        dto.setSubtotal(order.getSubtotal());
        dto.setTaxes(order.getTaxes());
        dto.setShippingFee(order.getShippingFee());
        dto.setDiscount(order.getDiscount());
        dto.setTotal(order.getTotal());
        return dto;
    }
}
