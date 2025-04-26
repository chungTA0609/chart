package com.example.chart.controllers;

import com.example.chart.models.Order;
import com.example.chart.models.OrderStatus;
import com.example.chart.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<Order> placeOrder(
            @RequestBody Order order,
            @RequestParam String paymentMethod) {
        return ResponseEntity.ok(orderService.placeOrder(order, paymentMethod));
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<Order> getOrderDetails(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrderDetails(orderId));
    }

    @PatchMapping("/orders/{orderId}/status")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable UUID orderId,
            @RequestParam OrderStatus status,
            @RequestParam(required = false) String trackingNumber) {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status, trackingNumber));
    }

    @GetMapping("/orders/history")
    public ResponseEntity<Page<Order>> getOrderHistory(
            @RequestParam UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateTo,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        return ResponseEntity.ok(orderService.getOrderHistory(userId, page, size, status, dateFrom, dateTo, sortBy, sortDirection));
    }
}
