package com.example.chart.controllers;

import com.example.chart.dto.orders.OrderSummaryDTO;
import com.example.chart.models.Address;
import com.example.chart.models.Order;
import com.example.chart.services.CheckoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CheckoutController {

    @Autowired
    private CheckoutService checkoutService;

    @GetMapping("/checkout/summary")
    public ResponseEntity<OrderSummaryDTO> getOrderSummary(
            @RequestParam UUID userId,
            @RequestParam UUID cartId) {
        return ResponseEntity.ok(checkoutService.getOrderSummary(userId, cartId));
    }

    @PatchMapping("/checkout/summary")
    public ResponseEntity<OrderSummaryDTO> updateOrderSummary(
            @RequestParam UUID userId,
            @RequestBody OrderSummaryDTO updatedSummary) {
        return ResponseEntity.ok(checkoutService.updateOrderSummary(userId, updatedSummary));
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<Address>> getUserAddresses(@RequestParam Long userId) {
        return ResponseEntity.ok(checkoutService.getUserAddresses(userId));
    }

    @PostMapping("/addresses")
    public ResponseEntity<Address> saveAddress(@RequestBody Address address) {
        return ResponseEntity.ok(checkoutService.saveAddress(address));
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<Address> updateAddress(
            @PathVariable Long addressId,
            @RequestBody Address address) {
        address.setId(addressId);
        return ResponseEntity.ok(checkoutService.saveAddress(address));
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<Void> deleteAddress(@PathVariable Long addressId) {
        checkoutService.deleteAddress(addressId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/shipping/methods")
    public ResponseEntity<List<String>> getShippingMethods(@RequestParam UUID addressId) {
        return ResponseEntity.ok(checkoutService.getShippingMethods(addressId));
    }

    @PostMapping("/promo/apply")
    public ResponseEntity<OrderSummaryDTO> applyPromoCode(
            @RequestParam String code,
            @RequestParam UUID orderId) {
        return ResponseEntity.ok(checkoutService.applyPromoCode(code, orderId));
    }

    @PostMapping("/payments")
    public ResponseEntity<Order> placeOrder(
            @RequestBody Order order,
            @RequestParam String paymentMethod) {
        return ResponseEntity.ok(checkoutService.placeOrder(order, paymentMethod));
    }

    @PostMapping("/payments/cod")
    public ResponseEntity<Order> placeCODOrder(@RequestBody Order order) {
        return ResponseEntity.ok(checkoutService.placeOrder(order, "COD"));
    }
}
