package com.example.chart.controllers;

import com.example.chart.dto.products.ProductFilterDTO;
import com.example.chart.dto.products.ProductRequestDTO;
import com.example.chart.dto.products.ProductResponseDTO;
import com.example.chart.dto.orders.StockUpdateRequestDTO;
import com.example.chart.dto.orders.StockUpdateResponseDTO;
import com.example.chart.services.ProductService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private ProductService productService;

    @PostMapping("/get-list")
    public Page<ProductResponseDTO> searchProducts(
            @RequestBody ProductFilterDTO productFilterDTO
    ) {
        Pageable pageable = PageRequest.of(productFilterDTO.getPage(), productFilterDTO.getSize());
        return productService.searchAndFilterProducts(productFilterDTO, pageable);
    }

    @RolesAllowed({"ADMIN"})
    @PostMapping("/create")
    public ProductResponseDTO createProduct(@Valid @RequestBody ProductRequestDTO requestDTO) {
        return productService.saveProduct(requestDTO);
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PatchMapping("/{id}/stock/decrease")
    public ResponseEntity<StockUpdateResponseDTO> decreaseStock(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateRequestDTO requestDTO
    ) {
        StockUpdateResponseDTO response = productService.decreaseStock(id, requestDTO);
        return response.isSuccess() ?
                ResponseEntity.ok(response) :
                ResponseEntity.badRequest().body(response);
    }

    @PatchMapping("/{id}/stock/increase")
    public ResponseEntity<StockUpdateResponseDTO> increaseStock(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateRequestDTO requestDTO
    ) {
        StockUpdateResponseDTO response = productService.increaseStock(id, requestDTO);
        return response.isSuccess() ?
                ResponseEntity.ok(response) :
                ResponseEntity.badRequest().body(response);
    }
}
