package com.example.chart.controllers;

import com.example.chart.dto.CheckoutDTO;
import com.example.chart.dto.OrderDTO;
import com.example.chart.models.ShippingMethod;
import com.example.chart.services.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;

    @GetMapping
    public ResponseEntity<CheckoutDTO> initializeCheckout(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(checkoutService.initializeCheckout(userId));
    }

    @PutMapping("/shipping-address")
    public ResponseEntity<CheckoutDTO> updateShippingAddress(
            @AuthenticationPrincipal Long userId,
            @RequestParam String shippingAddress,
            @RequestParam String billingAddress) {
        return ResponseEntity.ok(checkoutService.updateShippingAddress(userId, shippingAddress, billingAddress));
    }

    @PutMapping("/shipping-method")
    public ResponseEntity<CheckoutDTO> updateShippingMethod(
            @AuthenticationPrincipal Long userId,
            @RequestParam ShippingMethod shippingMethod) {
        return ResponseEntity.ok(checkoutService.updateShippingMethod(userId, shippingMethod));
    }

    @PostMapping("/promo-code")
    public ResponseEntity<CheckoutDTO> applyPromoCode(
            @AuthenticationPrincipal Long userId,
            @RequestParam String promoCode) {
        return ResponseEntity.ok(checkoutService.applyPromoCode(userId, promoCode));
    }

    @DeleteMapping("/promo-code")
    public ResponseEntity<CheckoutDTO> removePromoCode(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(checkoutService.removePromoCode(userId));
    }

    @PostMapping("/payment")
    public ResponseEntity<OrderDTO> processPayment(
            @AuthenticationPrincipal Long userId,
            @RequestParam String paymentMethod) {
        return ResponseEntity.ok(checkoutService.processPayment(userId, paymentMethod));
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateCheckout(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(checkoutService.validateCheckout(userId));
    }
}
