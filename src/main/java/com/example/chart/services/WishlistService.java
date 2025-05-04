package com.example.chart.services;

import com.example.chart.dto.carts.CartItemRequestDTO;
import com.example.chart.dto.carts.WishlistItemRequestDTO;
import com.example.chart.dto.carts.WishlistResponseDTO;
import com.example.chart.dto.WishlistDTO;
import com.example.chart.helpers.DtoMapper;
import com.example.chart.models.Wishlist;
import com.example.chart.models.WishlistItem;
import com.example.chart.repository.ProductRepository;
import com.example.chart.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

public interface WishlistService {

    WishlistDTO getWishlist(Long userId);
    WishlistDTO addItemToWishlist(Long userId, Long productId);
    WishlistDTO removeItemFromWishlist(Long userId, Long productId);
    boolean isProductInWishlist(Long userId, Long productId);
}
