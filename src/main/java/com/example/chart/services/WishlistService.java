package com.example.chart.services;

import com.example.chart.dto.carts.CartItemRequestDTO;
import com.example.chart.dto.carts.WishlistItemRequestDTO;
import com.example.chart.dto.carts.WishlistResponseDTO;
import com.example.chart.helpers.DtoMapper;
import com.example.chart.models.Products;
import com.example.chart.models.Wishlist;
import com.example.chart.models.WishlistItem;
import com.example.chart.repository.ProductRepository;
import com.example.chart.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class WishlistService {

    private WishlistRepository wishlistRepository;

    private ProductRepository productRepository;

    private CartService cartService;

    private DtoMapper dtoMapper;

    @Transactional
    public WishlistResponseDTO getWishlist(Long userId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseGet(() -> createNewWishlist(userId));
        return dtoMapper.mapToWishlistResponseDTO(wishlist);
    }

    @Transactional
    public WishlistResponseDTO addItemToWishlist(Long userId, WishlistItemRequestDTO requestDTO) {
        Products product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseGet(() -> createNewWishlist(userId));

        boolean itemExists = wishlist.getItems().stream()
                .anyMatch(item -> item.getProduct().getId().equals(requestDTO.getProductId()));

        if (!itemExists) {
            WishlistItem item = dtoMapper.mapToWishlistItemEntity(requestDTO, wishlist, product);
            if (wishlist.getItems() == null) {
                wishlist.setItems(new ArrayList<>());
            }
            wishlist.getItems().add(item);
            Wishlist savedWishlist = wishlistRepository.save(wishlist);
            return dtoMapper.mapToWishlistResponseDTO(savedWishlist);
        }

        return dtoMapper.mapToWishlistResponseDTO(wishlist);
    }

    @Transactional
    public WishlistResponseDTO removeItemFromWishlist(Long userId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        wishlist.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        return dtoMapper.mapToWishlistResponseDTO(savedWishlist);
    }

    @Transactional
    public WishlistResponseDTO moveItemToCart(Long userId, Long productId, Integer quantity) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));

        WishlistItem item = wishlist.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in wishlist"));

        CartItemRequestDTO cartItemRequest = new CartItemRequestDTO();
        cartItemRequest.setProductId(productId);
        cartItemRequest.setQuantity(quantity);

        cartService.addItemToCart(userId, cartItemRequest);

        wishlist.getItems().remove(item);
        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        return dtoMapper.mapToWishlistResponseDTO(savedWishlist);
    }

    private Wishlist createNewWishlist(Long userId) {
        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);
        wishlist.setItems(new ArrayList<>());
        return wishlistRepository.save(wishlist);
    }
}
