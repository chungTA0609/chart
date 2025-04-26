package com.example.chart.services;

import com.example.chart.dto.orders.StockChangeEvent;
import com.example.chart.dto.orders.StockUpdateRequestDTO;
import com.example.chart.dto.orders.StockUpdateResponseDTO;
import com.example.chart.dto.products.ProductFilterDTO;
import com.example.chart.dto.products.ProductRequestDTO;
import com.example.chart.dto.products.ProductResponseDTO;
import com.example.chart.helpers.DtoMapper;
import com.example.chart.models.Category;
import com.example.chart.models.Product;
import com.example.chart.repository.CategoryRepository;
import com.example.chart.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;
    private KafkaTemplate<String, StockChangeEvent> kafkaTemplate;
    private CategoryRepository categoryRepository;

    private DtoMapper dtoMapper;

    private static final String STOCK_TOPIC = "stock-updates";

    @Cacheable(value = "products", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #keyword + '-' + #categoryId + '-' + #minPrice + '-' + #maxPrice + '-' + #brand + '-' + #minRating + '-' + #sortBy + '-' + #sortDirection")
    public Page<ProductResponseDTO> searchAndFilterProducts(
            ProductFilterDTO productFilterDTO,
            Pageable pageable
    ) {
        Sort sort = Sort.by(
                productFilterDTO.getSortDirection().equalsIgnoreCase("desc") ?
                        Sort.Direction.DESC :
                        Sort.Direction.ASC,
                getSortField(productFilterDTO.getSortBy())
        );

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                sort
        );

        Page<Product> productPage = productRepository.searchAndFilter(
                productFilterDTO.getKeyword(), productFilterDTO.getCategoryId(), productFilterDTO.getMinPrice(), productFilterDTO.getMaxPrice(), productFilterDTO.getBrand(), productFilterDTO.getMinRating(), sortedPageable
        );

        List<ProductResponseDTO> dtos = productPage.getContent().stream()
                .map(dtoMapper::mapToProductResponseDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(dtos, pageable, productPage.getTotalElements());
    }

    private String getSortField(String sortBy) {
        return switch (sortBy.toLowerCase()) {
            case "price" -> "price";
            case "popularity" -> "popularity";
            case "newest" -> "createdDate";
            case "rating" -> "rating";
            default -> "id";
        };
    }

    @Transactional
    public ProductResponseDTO saveProduct(ProductRequestDTO requestDTO) {
        List<Category> categories = requestDTO.getCategoryIds() != null ?
                categoryRepository.findAllById(requestDTO.getCategoryIds()) :
                List.of();
        Product product = dtoMapper.mapToProductEntity(requestDTO, categories);
        Product savedProduct = productRepository.save(product);
        return dtoMapper.mapToProductResponseDTO(savedProduct);
    }

    @Cacheable(value = "product", key = "#id")
    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return dtoMapper.mapToProductResponseDTO(product);
    }

    @Transactional
    @CacheEvict(value = "product", key = "#productId")
    public StockUpdateResponseDTO decreaseStock(Long productId, StockUpdateRequestDTO requestDTO) {
        int updatedRows = productRepository.decreaseStock(productId, requestDTO.getQuantity());
        if (updatedRows > 0) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            publishStockChangeEvent(productId, -requestDTO.getQuantity(), product.getStockQuantity(), "DECREMENT");
            return new StockUpdateResponseDTO(true, "Stock decreased successfully");
        }
        return new StockUpdateResponseDTO(false, "Insufficient stock or update failed");
    }

    @Transactional
    @CacheEvict(value = "product", key = "#productId")
    public StockUpdateResponseDTO increaseStock(Long productId, StockUpdateRequestDTO requestDTO) {
        int updatedRows = productRepository.increaseStock(productId, requestDTO.getQuantity());
        if (updatedRows > 0) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            publishStockChangeEvent(productId, requestDTO.getQuantity(), product.getStockQuantity(), "INCREMENT");
            return new StockUpdateResponseDTO(true, "Stock increased successfully");
        }
        return new StockUpdateResponseDTO(false, "Stock update failed");
    }

    private void publishStockChangeEvent(Long productId, Integer quantityChanged, Integer newStockQuantity, String eventType) {
        StockChangeEvent event = new StockChangeEvent(
                productId,
                quantityChanged,
                newStockQuantity,
                eventType,
                LocalDateTime.now().toString()
        );
        kafkaTemplate.send(STOCK_TOPIC, event);
    }
}