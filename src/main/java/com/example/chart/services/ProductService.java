package com.example.chart.services;

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

@Service
@AllArgsConstructor
public class ProductService {

    private ProductRepository productRepository;
//    private KafkaTemplate<String, StockChangeEvent> kafkaTemplate;
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
                pageable.getPageNumber() - 1,
                pageable.getPageSize(),
                sort
        );

        Page<Products> productPage = productRepository.searchAndFilter(
                productFilterDTO.getKeyword(),
                productFilterDTO.getCategoryId(),
                productFilterDTO.getMinPrice(),
                productFilterDTO.getMaxPrice(),
                productFilterDTO.getBrand(),
                productFilterDTO.getMinRating(),
                sortedPageable
        );

        List<ProductResponseDTO> dtos = productPage.getContent().stream()
                .map(dtoMapper::mapToProductResponseDTO)
                .toList();

        return new PageImpl<>(dtos, sortedPageable, productPage.getTotalElements());
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
        Category categories = categoryRepository.findAllById(Collections.singleton(requestDTO.getCategoryIds())).getFirst();
        Products product = dtoMapper.mapToProductEntity(requestDTO, categories);
        Products savedProduct = productRepository.save(product);
        return dtoMapper.mapToProductResponseDTO(savedProduct);
    }

    @Cacheable(value = "product", key = "#id")
    public ProductResponseDTO getProductById(Long id) {
        Products product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return dtoMapper.mapToProductResponseDTO(product);
    }

    @Transactional
    @CacheEvict(value = "product", key = "#productId")
    public StockUpdateResponseDTO decreaseStock(Long productId, StockUpdateRequestDTO requestDTO) {
        int updatedRows = productRepository.decreaseStock(productId, requestDTO.getQuantity());
        if (updatedRows > 0) {
            Products product = productRepository.findById(productId)
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
            Products product = productRepository.findById(productId)
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
//        kafkaTemplate.send(STOCK_TOPIC, event);
    }

    private static <T> Page<T> listToPage(List<T> list, Pageable pageable) {
        int total = list.size();
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), total);

        List<T> content = (start > end) ? List.of() : list.subList(start, end);

        return new PageImpl<>(content, pageable, total);
    }

    @Transactional
//    @CacheEvict(value = {"product", "products"}, key = "#id")
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {
        Products existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        List<Category> categories = categoryRepository.findAllById(Collections.singleton(requestDTO.getCategoryIds()));
            Products updatedProduct;
        if (!categories.isEmpty()) {
            updatedProduct = dtoMapper.mapToProductEntity(requestDTO, categories.getFirst());
        } else {
            updatedProduct = dtoMapper.mapToProductEntity(requestDTO, new Category());
        }

        // Map updated fields to existing product
        updatedProduct.setId(existingProduct.getId());
        updatedProduct.setCreatedDate(existingProduct.getCreatedDate());
        updatedProduct.setPopularity(existingProduct.getPopularity());
        updatedProduct.setVersion(existingProduct.getVersion() == null ? 0 : existingProduct.getVersion() + 1);
        updatedProduct.setImages(existingProduct.getImages());
        updatedProduct.setColors(requestDTO.getColors());
        updatedProduct.setRating(requestDTO.getRating());
        List<Specifications> specifications = new ArrayList<>();
        for (Specifications spec : requestDTO.getSpecifications()) {
            Specifications specEntity = new Specifications();
            specEntity.setProduct(updatedProduct);
            specEntity.setKey(spec.getKey());
            specEntity.setValue(spec.getValue());
            specifications.add(specEntity);
        }
        List<Dimensions> dimensions = new ArrayList<>();
        for (Dimensions dimension : requestDTO.getDimensions()) {
            Dimensions dimensions1 = new Dimensions();
            dimensions1.setProduct(updatedProduct);
            dimensions1.setKey(dimension.getKey());
            dimensions1.setValue(dimension.getValue());
            dimensions.add(dimension);
        }
        updatedProduct.setDimensions(dimensions);
        updatedProduct.setSpecifications(specifications);
        // Check for stock change and publish event if necessary
        if (existingProduct.getStockQuantity() != null && !existingProduct.getStockQuantity().equals(requestDTO.getStockQuantity())) {
            int quantityChanged = requestDTO.getStockQuantity() - existingProduct.getStockQuantity();
            String eventType = quantityChanged > 0 ? "INCREMENT" : "DECREMENT";
            publishStockChangeEvent(id, quantityChanged, requestDTO.getStockQuantity(), eventType);
        }

        Products savedProduct = productRepository.save(updatedProduct);
        return dtoMapper.mapToProductResponseDTO(savedProduct);
    }
}