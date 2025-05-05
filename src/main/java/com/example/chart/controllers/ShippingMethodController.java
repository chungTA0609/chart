package com.example.chart.controllers;

import com.example.chart.core.ApiResponse;
import com.example.chart.dto.ShippingMethodDTO;
import com.example.chart.services.ShippingMethodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipping-methods")
@RequiredArgsConstructor
public class ShippingMethodController {
    private final ShippingMethodService shippingMethodService;

    @PostMapping
    public ResponseEntity<ApiResponse<ShippingMethodDTO>> createShippingMethod(
            @Valid @RequestBody ShippingMethodDTO shippingMethodDTO) {
        ShippingMethodDTO created = shippingMethodService.createShippingMethod(shippingMethodDTO);
        return ResponseEntity.ok(ApiResponse.success(created, "Shipping method created successfully"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ShippingMethodDTO>> updateShippingMethod(
            @PathVariable Long id,
            @Valid @RequestBody ShippingMethodDTO shippingMethodDTO) {
        ShippingMethodDTO updated = shippingMethodService.updateShippingMethod(id, shippingMethodDTO);
        return ResponseEntity.ok(ApiResponse.success(updated, "Shipping method updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteShippingMethod(@PathVariable Long id) {
        shippingMethodService.deleteShippingMethod(id);
        return ResponseEntity.ok(ApiResponse.success(null,"Shipping method deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShippingMethodDTO>> getShippingMethod(@PathVariable Long id) {
        ShippingMethodDTO shippingMethod = shippingMethodService.getShippingMethod(id);
        return ResponseEntity.ok(ApiResponse.success(shippingMethod, "Shipping method retrieved successfully"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ShippingMethodDTO>>> getAllActiveShippingMethods() {
        List<ShippingMethodDTO> shippingMethods = shippingMethodService.getAllActiveShippingMethods();
        return ResponseEntity.ok(ApiResponse.success(shippingMethods, "Active shipping methods retrieved successfully"));
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<Void>> toggleShippingMethodStatus(@PathVariable Long id) {
        shippingMethodService.toggleShippingMethodStatus(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Shipping method status toggled successfully"));
    }
} 