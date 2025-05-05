package com.example.chart.services;

import com.example.chart.dto.ProductDTO;
import org.springframework.data.domain.*;

public interface ProductService {

    ProductDTO saveProduct(ProductDTO productDTO);
    void deleteProduct(Long id);
    ProductDTO getProductById(Long id);
    Page<ProductDTO> getAllProducts(String keyword, String sortBy, String sortDirection, int page, int size);
    Page<ProductDTO> searchProducts(Long categoryId, String brand, Double minPrice, Double maxPrice, Pageable pageable);
    void updateStock(Long productId, Integer quantity);
//    Page<ProductVariantDTO> getProductVariants(Long productId, Pageable pageable);
//    ProductVariantDTO addProductVariant(Long productId, ProductVariantDTO variantDTO);
//    ProductVariantDTO updateProductVariant(ProductVariantDTO variantDTO);
//    void deleteProductVariant(Long variantId);
}