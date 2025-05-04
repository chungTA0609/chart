package com.example.chart.helpers;

import com.example.chart.dto.carts.*;
import com.example.chart.dto.orders.OrderItemDTO;
import com.example.chart.dto.orders.OrderSummaryDTO;
import com.example.chart.dto.products.ProductRequestDTO;
import com.example.chart.dto.products.ProductResponseDTO;
import com.example.chart.dto.products.ProductVariantRequestDTO;
import com.example.chart.dto.products.ProductVariantResponseDTO;
import com.example.chart.models.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoMapper {
//    // Existing methods unchanged, only adding new methods
//
//    public Product mapToProductEntity(ProductRequestDTO dto, Category categories) {
//        Product product = new Product();
//        product.setName(dto.getName());
//        product.setDescription(dto.getDescription());
//        product.setImages(dto.getImages());
////        product.setCategories(categories);
//        product.setPrice(dto.getPrice());
//        product.setStockQuantity(dto.getStockQuantity());
//        product.setBrand(dto.getBrand());
//        if (dto.getVariants() != null) {
//            List<ProductVariant> variants = dto.getVariants().stream()
//                    .map(v -> mapToProductVariantEntity(v, product))
//                    .collect(Collectors.toList());
//            product.setVariants(variants);
//        }
//        product.setRating(dto.getRating());
//        return product;
//    }
//
//    public ProductVariant mapToProductVariantEntity(ProductVariantRequestDTO dto, Products product) {
//        ProductVariant variant = new ProductVariant();
//        variant.setProduct(product);
//        variant.setSize(dto.getSize());
//        variant.setColor(dto.getColor());
//        variant.setOtherAttributes(dto.getOtherAttributes());
//        variant.setPrice(dto.getPrice());
//        variant.setStockQuantity(dto.getStockQuantity());
//        return variant;
//    }
//
//    public Category mapToCategoryEntity(CategoryRequestDTO dto, Category parentCategory) {
//        Category category = new Category();
//        category.setName(dto.getName());
//        category.setParentCategory(parentCategory);
//        return category;
//    }
//
//    public CartItem mapToCartItemEntity(CartItemRequestDTO dto, Cart cart, Products product) {
//        CartItem item = new CartItem();
//        item.setCart(cart);
//        item.setProduct(product);
//        item.setQuantity(dto.getQuantity());
//        return item;
//    }
//
//    public WishlistItem mapToWishlistItemEntity(WishlistItemRequestDTO dto, Wishlist wishlist, Products product) {
//        WishlistItem item = new WishlistItem();
//        item.setWishlist(wishlist);
//        item.setProduct(product);
//        return item;
//    }
//
//    public ProductResponseDTO mapToProductResponseDTO(Products product) {
//        ProductResponseDTO dto = new ProductResponseDTO();
//        dto.setId(product.getId());
//        dto.setName(product.getName());
//        dto.setDescription(product.getDescription());
//        dto.setImages(product.getImages());
//        dto.setCategories(product.getCategories());
//        dto.setPrice(product.getPrice());
//        dto.setStockQuantity(product.getStockQuantity());
//        dto.setBrand(product.getBrand());
//        dto.setVariants(product.getVariants().stream()
//                .map(this::mapToProductVariantResponseDTO)
//                .collect(Collectors.toList()));
//        dto.setRating(product.getRating());
//        dto.setPopularity(product.getPopularity());
//        dto.setCreatedDate(product.getCreatedDate());
//        return dto;
//    }
//
//    public ProductVariantResponseDTO mapToProductVariantResponseDTO(ProductVariant variant) {
//        ProductVariantResponseDTO dto = new ProductVariantResponseDTO();
//        dto.setId(variant.getId());
//        dto.setSize(variant.getSize());
//        dto.setColor(variant.getColor());
//        dto.setOtherAttributes(variant.getOtherAttributes());
//        dto.setPrice(variant.getPrice());
//        dto.setStockQuantity(variant.getStockQuantity());
//        return dto;
//    }
//
//    public CategoryResponseDTO mapToCategoryResponseDTO(Category category) {
//        CategoryResponseDTO dto = new CategoryResponseDTO();
//        dto.setId(category.getId());
//        dto.setName(category.getName());
//        if (category.getParentCategory() != null) {
//            CategoryResponseDTO parentDTO = new CategoryResponseDTO();
//            parentDTO.setId(category.getParentCategory().getId());
//            parentDTO.setName(category.getParentCategory().getName());
//            dto.setParentCategory(parentDTO);
//        }
//        dto.setSubCategories(category.getSubCategories().stream()
//                .map(this::mapToCategoryResponseDTO)
//                .collect(Collectors.toList()));
//        return dto;
//    }
//
//    public CartResponseDTO mapToCartResponseDTO(Cart cart) {
//        CartResponseDTO dto = new CartResponseDTO();
//        dto.setId(cart.getId());
//        dto.setUserId(cart.getUserId());
//        dto.setItems(cart.getItems().stream()
//                .map(this::mapToCartItemResponseDTO)
//                .collect(Collectors.toList()));
//        dto.setSubtotal(calculateSubtotal(cart));
//        dto.setTotal(dto.getSubtotal()); // For simplicity, total = subtotal (no taxes/discounts)
//        return dto;
//    }
//
//    public CartItemResponseDTO mapToCartItemResponseDTO(CartItem item) {
//        CartItemResponseDTO dto = new CartItemResponseDTO();
//        dto.setId(item.getId());
//        dto.setProductId(item.getProduct().getId());
//        dto.setProductName(item.getProduct().getName());
//        dto.setPrice(item.getProduct().getPrice());
//        dto.setQuantity(item.getQuantity());
//        dto.setItemTotal(item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())));
//        return dto;
//    }
//
//    public WishlistResponseDTO mapToWishlistResponseDTO(Wishlist wishlist) {
//        WishlistResponseDTO dto = new WishlistResponseDTO();
//        dto.setId(wishlist.getId());
//        dto.setUserId(wishlist.getUserId());
//        dto.setItems(wishlist.getItems().stream()
//                .map(this::mapToWishlistItemResponseDTO)
//                .collect(Collectors.toList()));
//        return dto;
//    }
//
//    public WishlistItemResponseDTO mapToWishlistItemResponseDTO(WishlistItem item) {
//        WishlistItemResponseDTO dto = new WishlistItemResponseDTO();
//        dto.setId(item.getId());
//        dto.setProductId(item.getProduct().getId());
//        dto.setProductName(item.getProduct().getName());
//        return dto;
//    }
//
//    private BigDecimal calculateSubtotal(Cart cart) {
//        return cart.getItems().stream()
//                .map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//    }
//
//    private OrderSummaryDTO mapToOrderSummaryDTO(Order order) {
//        OrderSummaryDTO dto = new OrderSummaryDTO();
//        dto.setItems(order.getItems().stream()
//                .map(item -> {
//                    OrderItemDTO itemDTO = new OrderItemDTO();
//                    itemDTO.setSku(item.getSku());
//                    itemDTO.setQuantity(item.getQuantity());
//                    itemDTO.setPrice(item.getPrice());
//                    return itemDTO;
//                })
//                .toList());
//        dto.setSubtotal(order.getSubtotal());
//        dto.setTaxes(order.getTaxes());
//        dto.setShippingFee(order.getShippingFee());
//        dto.setDiscount(order.getDiscount());
//        dto.setTotal(order.getTotal());
//        return dto;
//    }
}
