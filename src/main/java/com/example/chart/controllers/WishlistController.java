package com.example.chart.controllers;

import com.example.chart.dto.carts.WishlistItemRequestDTO;
import com.example.chart.dto.carts.WishlistResponseDTO;
import com.example.chart.services.WishlistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/{userId}")
    public WishlistResponseDTO getWishlist(@PathVariable Long userId) {
        return wishlistService.getWishlist(userId);
    }

    @PostMapping("/{userId}/items")
    public WishlistResponseDTO addItemToWishlist(
            @PathVariable Long userId,
            @Valid @RequestBody WishlistItemRequestDTO requestDTO
    ) {
        return wishlistService.addItemToWishlist(userId, requestDTO);
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<WishlistResponseDTO> removeItemFromWishlist(
            @PathVariable Long userId,
            @PathVariable Long productId
    ) {
        WishlistResponseDTO response = wishlistService.removeItemFromWishlist(userId, productId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/items/{productId}/move-to-cart")
    public WishlistResponseDTO moveItemToCart(
            @PathVariable Long userId,
            @PathVariable Long productId,
            @RequestParam(defaultValue = "1") Integer quantity
    ) {
        return wishlistService.moveItemToCart(userId, productId, quantity);
    }
}
