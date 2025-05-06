package com.example.chart.services.impl;

import com.example.chart.dto.carts.CartDTO;
import com.example.chart.dto.CheckoutDTO;
import com.example.chart.dto.OrderDTO;
import com.example.chart.dto.OrderItemDTO;
import com.example.chart.models.*;
import com.example.chart.repository.*;
import com.example.chart.services.CartService;
import com.example.chart.services.CheckoutService;
import com.example.chart.services.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckoutServiceImpl implements CheckoutService {
    private final CartService cartService;
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final ShippingMethodRepository shippingMethodRepository;

    private OrderDTO convertCartToOrder(CartDTO cartDTO) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(cartDTO.getUserId());
        orderDTO.setItems(cartDTO.getItems().stream()
                .map(cartItem -> {
                    OrderItemDTO orderItem = new OrderItemDTO();
                    orderItem.setProductId(cartItem.getProductId());
                    orderItem.setQuantity(cartItem.getQuantity());
                    orderItem.setPrice(cartItem.getPrice());
                    return orderItem;
                })
                .collect(Collectors.toSet()));
        orderDTO.setTotalAmount(cartDTO.getTotalPrice());
        return orderDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public CheckoutDTO initializeCheckout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        CheckoutDTO checkoutDTO = new CheckoutDTO();
        checkoutDTO.setUserId(userId);
        
        // Get cart items
        CartDTO cartDTO = cartService.getCart(userId);
        OrderDTO orderDTO = convertCartToOrder(cartDTO);
        checkoutDTO.setItems(orderDTO.getItems());
        
        // Calculate initial totals
        return calculateTotals(checkoutDTO);
    }

    @Override
    @Transactional
    public CheckoutDTO updateShippingAddress(Long userId, Long shippingAddressId, Long billingAddressId) {
        CheckoutDTO checkoutDTO = initializeCheckout(userId);
        
        // Get shipping address
        Address shippingAddress = addressRepository.findById(shippingAddressId)
                .orElseThrow(() -> new EntityNotFoundException("Shipping address not found"));
        
        // Get billing address
//        Address billingAddress = addressRepository.findById(billingAddressId)
//                .orElseThrow(() -> new EntityNotFoundException("Billing address not found"));
        
        // Format addresses
        String formattedShippingAddress = formatAddress(shippingAddress);
//        String formattedBillingAddress = formatAddress(billingAddress);
        
        checkoutDTO.setSelectedAddressId(shippingAddressId);
//        checkoutDTO.setBillingAddress(formattedBillingAddress);
        return calculateTotals(checkoutDTO);
    }

    private String formatAddress(Address address) {
        return String.format("%s, %s, %s, %s, %s, Phone: %s",
                address.getStreet(),
                address.getCity(),
                address.getState(),
                address.getZipCode(),
                address.getCountry(),
                address.getPhoneNumber());
    }

    @Override
    @Transactional
    public CheckoutDTO updateShippingMethod(Long userId, ShippingMethod shippingMethod) {
        CheckoutDTO checkoutDTO = initializeCheckout(userId);
        checkoutDTO.setShippingMethodId(shippingMethod.getId());
        return calculateTotals(checkoutDTO);
    }

    @Override
    @Transactional
    public CheckoutDTO applyPromoCode(Long userId, String promoCode) {
        CheckoutDTO checkoutDTO = initializeCheckout(userId);
        
        PromoCode code = promoCodeRepository.findByCodeAndActiveTrue(promoCode)
                .orElseThrow(() -> new EntityNotFoundException("Invalid promo code"));

        checkoutDTO.setPromoCode(promoCode);
        return calculateTotals(checkoutDTO);
    }

    @Override
    @Transactional
    public CheckoutDTO removePromoCode(Long userId) {
        CheckoutDTO checkoutDTO = initializeCheckout(userId);
        checkoutDTO.setPromoCode(null);
        return calculateTotals(checkoutDTO);
    }

    @Override
    @Transactional
    public CheckoutDTO calculateTotals(Long userId) {
        return calculateTotals(initializeCheckout(userId));
    }

    private CheckoutDTO calculateTotals(CheckoutDTO checkoutDTO) {
        // Calculate subtotal
        BigDecimal subtotal = checkoutDTO.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        checkoutDTO.setSubtotal(subtotal);

        // Calculate shipping cost
        BigDecimal shippingCost = BigDecimal.ZERO;
        if (checkoutDTO.getShippingMethodId() != null) {
            ShippingMethod shippingMethod = shippingMethodRepository.getReferenceById(checkoutDTO.getShippingMethodId());
            shippingCost = BigDecimal.valueOf(shippingMethod.getPrice());
        }
//        checkoutDTO.setShippingCost(shippingCost);

        // Calculate discount
        BigDecimal discount = BigDecimal.ZERO;
        if (checkoutDTO.getPromoCode() != null) {
            PromoCode promoCode = promoCodeRepository.findByCodeAndActiveTrue(checkoutDTO.getPromoCode())
                    .orElseThrow(() -> new EntityNotFoundException("Invalid promo code"));
            discount = promoCode.calculateDiscount(subtotal);
        }
        checkoutDTO.setDiscount(discount);

        // Calculate total
        BigDecimal total = subtotal.add(shippingCost).subtract(discount);
        checkoutDTO.setTotalAmount(total);

        return checkoutDTO;
    }

    @Override
    @Transactional
    public OrderDTO processPayment(Long userId, String paymentMethod) {
        if (!validateCheckout(userId)) {
            throw new IllegalStateException("Checkout validation failed");
        }

        CheckoutDTO checkoutDTO = calculateTotals(userId);
        
        // Create order from checkout
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setUserId(userId);
        orderDTO.setItems(checkoutDTO.getItems());
//        orderDTO.setShippingAddress(checkoutDTO.getShippingAddress());
//        orderDTO.setBillingAddress(checkoutDTO.getBillingAddress());
        orderDTO.setPaymentMethod(paymentMethod);
        orderDTO.setTotalAmount(checkoutDTO.getTotalAmount());

        // Process payment (integrate with payment gateway here)
        // For now, we'll just create the order
        return orderService.createOrder(userId, orderDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean validateCheckout(Long userId) {
        CheckoutDTO checkoutDTO = calculateTotals(userId);
        
        // Validate shipping address
//        if (checkoutDTO.getShippingAddress() == null || checkoutDTO.getShippingAddress().isEmpty()) {
//            return false;
//        }

        // Validate shipping method
//        if (checkoutDTO.getShippingMethod() == null || checkoutDTO.getShippingMethod().isEmpty()) {
//            return false;
//        }

        // Validate payment method
//        if (checkoutDTO.getPaymentMethod() == null || checkoutDTO.getPaymentMethod().isEmpty()) {
//            return false;
//        }

        // Validate items
        if (checkoutDTO.getItems() == null || checkoutDTO.getItems().isEmpty()) {
            return false;
        }

        // Validate promo code if present
        if (checkoutDTO.getPromoCode() != null) {
            PromoCode promoCode = promoCodeRepository.findByCodeAndActiveTrue(checkoutDTO.getPromoCode())
                    .orElseThrow(() -> new EntityNotFoundException("Invalid promo code"));
            if (!promoCode.isValid()) {
                return false;
            }
        }

        return true;
    }
} 