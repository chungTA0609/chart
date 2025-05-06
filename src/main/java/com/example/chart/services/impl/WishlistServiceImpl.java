package com.example.chart.services.impl;

import com.example.chart.dto.products.ProductDTO;
import com.example.chart.dto.products.ProductImageDTO;
import com.example.chart.dto.carts.WishlistDTO;
import com.example.chart.models.*;
import com.example.chart.repository.ProductRepository;
import com.example.chart.repository.UserRepository;
import com.example.chart.repository.WishlistRepository;
import com.example.chart.services.WishlistService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public WishlistDTO getWishlist(Long userId) {
        Wishlist wishlist = wishlistRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));
        return mapToDTO(wishlist);
    }

    @Override
    @Transactional
    public WishlistDTO addItemToWishlist(Long userId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByUserIdAndActiveTrue(userId)
                .orElseGet(() -> createNewWishlist(userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        if (!wishlist.getProducts().contains(product)) {
            wishlist.getProducts().add(product);
            wishlist = wishlistRepository.save(wishlist);
        }

        return mapToDTO(wishlist);
    }

    @Override
    @Transactional
    public WishlistDTO removeItemFromWishlist(Long userId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new EntityNotFoundException("Wishlist not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        wishlist.getProducts().remove(product);
        return mapToDTO(wishlistRepository.save(wishlist));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isProductInWishlist(Long userId, Long productId) {
        return wishlistRepository.findByUserIdAndActiveTrue(userId)
                .map(wishlist -> wishlist.getProducts().stream()
                        .anyMatch(product -> product.getId().equals(productId)))
                .orElse(false);
    }

    private Wishlist createNewWishlist(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Wishlist wishlist = new Wishlist();
        wishlist.setUser(user);
        wishlist.setActive(true);
        return wishlistRepository.save(wishlist);
    }

    private WishlistDTO mapToDTO(Wishlist wishlist) {
        WishlistDTO dto = new WishlistDTO();
        dto.setId(wishlist.getId());
        dto.setUserId(wishlist.getUser().getId());
        dto.setActive(wishlist.isActive());

        dto.setProducts(wishlist.getProducts().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toSet()));

        return dto;
    }

    private ProductDTO mapToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
//        dto.setBrand(product.getBrand());
        dto.setActive(product.isActive());
        dto.setRating(product.getRating());
        dto.setReviewCount(product.getReviewCount());

        dto.setImages(product.getImages().stream()
                .map(image -> {
                    ProductImageDTO imageDTO = new ProductImageDTO();
                    imageDTO.setId(image.getId());
                    imageDTO.setImageUrl(image.getImageUrl());
                    imageDTO.setMain(image.isMain());
                    return imageDTO;
                })
                .collect(Collectors.toList()));

        return dto;
    }
} 