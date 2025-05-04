package com.example.chart.services;

import com.example.chart.dto.CartDTO;
import com.example.chart.dto.CartItemDTO;

public interface CartService {

    CartDTO getCart(Long userId);
    CartDTO removeItemFromCart(Long userId, Long itemId);
    void clearCart(Long userId);
    CartDTO addItemToCart(Long userId, CartItemDTO cartItemDTO);
    CartDTO updateCartItem(Long userId, Long itemId, CartItemDTO cartItemDTO);
}
