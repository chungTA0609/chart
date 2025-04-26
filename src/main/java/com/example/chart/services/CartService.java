package com.example.chart.services;

import com.example.chart.dto.carts.CartItemRequestDTO;
import com.example.chart.dto.carts.CartResponseDTO;
import com.example.chart.helpers.DtoMapper;
import com.example.chart.models.Cart;
import com.example.chart.models.CartItem;
import com.example.chart.models.Product;
import com.example.chart.repository.CartRepository;
import com.example.chart.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService {

    private CartRepository cartRepository;

    private ProductRepository productRepository;

    private DtoMapper dtoMapper;

    @Transactional
    public CartResponseDTO getCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));
        return dtoMapper.mapToCartResponseDTO(cart);
    }

    @Transactional
    public CartResponseDTO addItemToCart(Long userId, CartItemRequestDTO requestDTO) {
        Product product = productRepository.findById(requestDTO.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (product.getStockQuantity() < requestDTO.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> createNewCart(userId));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(requestDTO.getProductId()))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + requestDTO.getQuantity();
            if (product.getStockQuantity() < newQuantity) {
                throw new RuntimeException("Insufficient stock");
            }
            item.setQuantity(newQuantity);
        } else {
            CartItem item = dtoMapper.mapToCartItemEntity(requestDTO, cart, product);
            if (cart.getItems() == null) {
                cart.setItems(new ArrayList<>());
            }
            cart.getItems().add(item);
        }

        Cart savedCart = cartRepository.save(cart);
        return dtoMapper.mapToCartResponseDTO(savedCart);
    }

    @Transactional
    public CartResponseDTO updateItemQuantity(Long userId, Long productId, CartItemRequestDTO requestDTO) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStockQuantity() < requestDTO.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        item.setQuantity(requestDTO.getQuantity());
        Cart savedCart = cartRepository.save(cart);
        return dtoMapper.mapToCartResponseDTO(savedCart);
    }

    @Transactional
    public CartResponseDTO removeItemFromCart(Long userId, Long productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        Cart savedCart = cartRepository.save(cart);
        return dtoMapper.mapToCartResponseDTO(savedCart);
    }

    private Cart createNewCart(Long userId) {
        Cart cart = new Cart();
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>());
        return cartRepository.save(cart);
    }
}
