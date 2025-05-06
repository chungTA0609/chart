package com.example.chart.services.impl;

import com.example.chart.dto.carts.CartDTO;
import com.example.chart.dto.orders.CartItemDTO;
import com.example.chart.models.*;
import com.example.chart.repository.CartItemRepository;
import com.example.chart.repository.CartRepository;
import com.example.chart.repository.ProductRepository;
import com.example.chart.repository.UserRepository;
import com.example.chart.services.CartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public CartDTO getCart(Long userId) {
        Cart cart = cartRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));
        return mapToDTO(cart);
    }

    @Override
    @Transactional
    public CartDTO addItemToCart(Long userId, CartItemDTO cartItemDTO) {
        Cart cart = cartRepository.findByUserIdAndActiveTrue(userId)
                .orElseGet(() -> createNewCart(userId));

        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        CartItem existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + cartItemDTO.getQuantity());
            existingItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity())));
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(cartItemDTO.getQuantity());
            newItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartItemDTO.getQuantity())));
            cart.getItems().add(newItem);
        }

        updateCartTotals(cart);
        return mapToDTO(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartDTO updateCartItem(Long userId, Long itemId, CartItemDTO cartItemDTO) {
        Cart cart = cartRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Item does not belong to user's cart");
        }

        item.setQuantity(cartItemDTO.getQuantity());
        item.setPrice(item.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItemDTO.getQuantity())));
        cartItemRepository.save(item);

        updateCartTotals(cart);
        return mapToDTO(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartDTO removeItemFromCart(Long userId, Long itemId) {
        Cart cart = cartRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));

        if (!item.getCart().getId().equals(cart.getId())) {
            throw new IllegalArgumentException("Item does not belong to user's cart");
        }

        cart.getItems().remove(item);
        cartItemRepository.delete(item);

        updateCartTotals(cart);
        return mapToDTO(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserIdAndActiveTrue(userId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found"));

        cartItemRepository.deleteByCartId(cart.getId());
        cart.getItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setTotalItems(0);
        cartRepository.save(cart);
    }

    private Cart createNewCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Cart cart = new Cart();
        cart.setUser(user);
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setTotalItems(0);
        cart.setActive(true);
        return cartRepository.save(cart);
    }

    private void updateCartTotals(Cart cart) {
        BigDecimal totalPrice = cart.getItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalItems = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();

        cart.setTotalPrice(totalPrice);
        cart.setTotalItems(totalItems);
    }

    private CartDTO mapToDTO(Cart cart) {
        CartDTO dto = new CartDTO();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setTotalPrice(cart.getTotalPrice());
        dto.setTotalItems(cart.getTotalItems());
        dto.setActive(cart.isActive());

        dto.setItems(cart.getItems().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toSet()));

        return dto;
    }

    private CartItemDTO mapToDTO(CartItem item) {
        CartItemDTO dto = new CartItemDTO();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        dto.setSubtotal(item.getSubtotal());
        return dto;
    }
} 