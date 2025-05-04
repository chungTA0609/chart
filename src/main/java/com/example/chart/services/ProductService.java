package com.example.chart.services;

import com.example.chart.dto.ProductDTO;
import com.example.chart.dto.orders.StockChangeEvent;
import com.example.chart.dto.orders.StockUpdateRequestDTO;
import com.example.chart.dto.orders.StockUpdateResponseDTO;
import com.example.chart.dto.products.ProductFilterDTO;
import com.example.chart.dto.products.ProductRequestDTO;
import com.example.chart.dto.products.ProductResponseDTO;
import com.example.chart.helpers.DtoMapper;
import com.example.chart.models.*;
import com.example.chart.repository.CategoryRepository;
import com.example.chart.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public interface ProductService {

    ProductDTO createProduct(ProductDTO productDTO);
    ProductDTO updateProduct(Long id, ProductDTO productDTO);
    void deleteProduct(Long id);
    ProductDTO getProductById(Long id);
    Page<ProductDTO> getAllProducts(String keyword, String sortBy, String sortDirection, int page, int size);
    Page<ProductDTO> searchProducts(Long categoryId, String brand, Double minPrice, Double maxPrice, Pageable pageable);
    void updateStock(Long productId, Integer quantity);
}