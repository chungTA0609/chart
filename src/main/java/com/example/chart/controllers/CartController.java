package com.example.chart.controllers;

import com.example.chart.dto.carts.CartItemRequestDTO;
import com.example.chart.dto.carts.CartResponseDTO;
import com.example.chart.services.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public CartResponseDTO getCart(@PathVariable Long userId) {
        return cartService.getCart(userId);
    }

    @PostMapping("/{userId}/items")
    public CartResponseDTO addItemToCart(
            @PathVariable Long userId,
            @Valid @RequestBody CartItemRequestDTO requestDTO
    ) {
        return cartService.addItemToCart(userId, requestDTO);
    }

    @PatchMapping("/{userId}/items/{productId}")
    public CartResponseDTO updateItemQuantity(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @Valid @RequestBody CartItemRequestDTO requestDTO
    ) {
        return cartService.updateItemQuantity(userId, productId, requestDTO);
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartResponseDTO> removeItemFromCart(
            @PathVariable Long userId,
            @PathVariable Long productId
    ) {
        CartResponseDTO response = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok(response);
    }
}
