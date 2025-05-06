package com.example.chart.services;

import com.example.chart.dto.carts.WishlistDTO;

public interface WishlistService {

    WishlistDTO getWishlist(Long userId);
    WishlistDTO addItemToWishlist(Long userId, Long productId);
    WishlistDTO removeItemFromWishlist(Long userId, Long productId);
    boolean isProductInWishlist(Long userId, Long productId);
}
