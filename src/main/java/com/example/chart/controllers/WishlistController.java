package com.example.chart.controllers;

import com.example.chart.dto.WishlistDTO;
import com.example.chart.services.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;

    @GetMapping
    public ResponseEntity<WishlistDTO> getWishlist(@AuthenticationPrincipal Long userId) {
        return ResponseEntity.ok(wishlistService.getWishlist(userId));
    }

    @PostMapping("/items/{productId}")
    public ResponseEntity<WishlistDTO> addItemToWishlist(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.addItemToWishlist(userId, productId));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<WishlistDTO> removeItemFromWishlist(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.removeItemFromWishlist(userId, productId));
    }

    @GetMapping("/items/{productId}/check")
    public ResponseEntity<Boolean> isProductInWishlist(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.isProductInWishlist(userId, productId));
    }
}
