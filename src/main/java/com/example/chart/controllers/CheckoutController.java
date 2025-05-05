package com.example.chart.controllers;

import com.example.chart.core.ApiResponse;
import com.example.chart.dto.CheckoutDTO;
import com.example.chart.dto.OrderDTO;
import com.example.chart.models.PromoCode;
import com.example.chart.models.ShippingMethod;
import com.example.chart.repository.PromoCodeRepository;
import com.example.chart.repository.UserRepository;
import com.example.chart.services.CheckoutService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final UserRepository userRepository;
    private final PromoCodeRepository promoCodeRepository;

    private Long getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found"))
                .getId();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<CheckoutDTO>> initializeCheckout() {
        return ResponseEntity.ok(ApiResponse.success(
            checkoutService.initializeCheckout(getCurrentUserId()),
            "Checkout initialized successfully"
        ));
    }

    @PutMapping("/shipping-address")
    public ResponseEntity<ApiResponse<CheckoutDTO>> updateShippingAddress(
            @RequestParam Long shippingAddressId
//            , @RequestParam Long billingAddressId
    ) {
        return ResponseEntity.ok(ApiResponse.success(
            checkoutService.updateShippingAddress(getCurrentUserId(), shippingAddressId, 1L),
            "Shipping address updated successfully"
        ));
    }

    @PutMapping("/shipping-method")
    public ResponseEntity<ApiResponse<CheckoutDTO>> updateShippingMethod(
            @RequestParam ShippingMethod shippingMethod) {
        return ResponseEntity.ok(ApiResponse.success(
            checkoutService.updateShippingMethod(getCurrentUserId(), shippingMethod),
            "Shipping method updated successfully"
        ));
    }

    @PostMapping("/promo-code")
    public ResponseEntity<ApiResponse<CheckoutDTO>> applyPromoCode(
            @RequestBody String promoCode) {
        return ResponseEntity.ok(ApiResponse.success(
            checkoutService.applyPromoCode(getCurrentUserId(), promoCode),
            "Promo code applied successfully"
        ));
    }

    @PostMapping("/promo-code/calculate")
    public ResponseEntity<ApiResponse<BigDecimal>> calculatePromoCodeDiscount(
            @RequestParam String promoCode) {
        PromoCode promoCoded = promoCodeRepository.findByCodeAndActiveTrue(promoCode)
                .orElseThrow(() -> new EntityNotFoundException("Invalid promo code"));

        return ResponseEntity.ok(ApiResponse.success(
            promoCoded.calculateDiscount(BigDecimal.valueOf(0)),
            "Discount calculated successfully"
        ));
    }

    @DeleteMapping("/promo-code")
    public ResponseEntity<ApiResponse<CheckoutDTO>> removePromoCode() {
        return ResponseEntity.ok(ApiResponse.success(
            checkoutService.removePromoCode(getCurrentUserId()),
            "Promo code removed successfully"
        ));
    }

    @PostMapping("/payment")
    public ResponseEntity<ApiResponse<OrderDTO>> processPayment(
            @RequestParam String paymentMethod) {
        return ResponseEntity.ok(ApiResponse.success(
            checkoutService.processPayment(getCurrentUserId(), paymentMethod),
            "Payment processed successfully"
        ));
    }

    @GetMapping("/validate")
    public ResponseEntity<ApiResponse<Boolean>> validateCheckout() {
        return ResponseEntity.ok(ApiResponse.success(
            checkoutService.validateCheckout(getCurrentUserId()),
            "Checkout validation completed"
        ));
    }
}
