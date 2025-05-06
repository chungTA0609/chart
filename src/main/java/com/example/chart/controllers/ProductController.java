package com.example.chart.controllers;

import com.example.chart.core.ApiResponse;
import com.example.chart.dto.products.ProductDTO;
import com.example.chart.dto.products.ProductSearchDTO;
import com.example.chart.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDTO>> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        return ResponseEntity.ok(ApiResponse.success(productService.saveProduct(productDTO), "Product created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO) {
        productDTO.setId(id);
        return ResponseEntity.ok(ApiResponse.success(productService.saveProduct(productDTO), "Product updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDTO>> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(productService.getProductById(id), "Product retrieved successfully"));
    }

    @PostMapping("/get-list")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getAllProducts(@RequestBody ProductSearchDTO searchDTO) {
        return ResponseEntity.ok(ApiResponse.success(
            productService.getAllProducts(
                searchDTO.getKeyword(),
                searchDTO.getSortBy(),
                searchDTO.getSortDirection(),
                searchDTO.getPage(),
                searchDTO.getSize()
            ),
            "Products retrieved successfully"
        ));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> searchProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(
            productService.searchProducts(categoryId, brand, minPrice, maxPrice, pageable),
            "Products retrieved successfully"
        ));
    }
}
