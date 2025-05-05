package com.example.chart.services.impl;

import com.example.chart.dto.*;
import com.example.chart.models.*;
import com.example.chart.repository.*;
import com.example.chart.services.ProductService;
import com.example.chart.services.ColorService;
import com.example.chart.services.ProductSpecificationService;
import com.example.chart.services.ProductDimensionService;
import com.example.chart.services.ProductImageService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageService productImageService;
    private final ColorService colorService;
    private final ProductSpecificationService productSpecificationService;
    private final ProductDimensionService productDimensionService;

    @Override
    @Transactional
    public ProductDTO saveProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Product product;
        if (productDTO.getId() != null) {
            // Update existing product
            product = productRepository.findById(productDTO.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        } else {
            // Create new product
            product = new Product();
        }

        // Set common fields
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setLongDescription(productDTO.getLongDescription());
        product.setPrice(productDTO.getPrice());
        product.setMainImage(productDTO.getMainImage());
        product.setStockQuantity(productDTO.getStockQuantity());
        product.setBrand(productDTO.getBrand());
        product.setCategory(category);
        product.setActive(productDTO.isActive());
        product.setRating(productDTO.getRating());
        product.setReviewCount(productDTO.getReviewCount());

        // Save the product first to ensure it has a valid ID
        Product savedProduct = productRepository.save(product);

        // Handle images using ProductImageService
        if (productDTO.getImages() != null) {
            productImageService.saveImages(savedProduct.getId(), productDTO.getImages());
        }

        // Handle colors using ColorService
        if (productDTO.getColors() != null) {
            colorService.saveColors(savedProduct.getId(), productDTO.getColors());
        }

        // Handle sizes
        if (productDTO.getSizes() != null) {
            if (product.getId() != null) {
                product.getSizes().clear();
            }
            product.setSizes(new HashSet<>(productDTO.getSizes()));
        }

        // Handle specifications using ProductSpecificationService
        if (productDTO.getSpecifications() != null) {
            productSpecificationService.saveSpecifications(savedProduct.getId(), productDTO.getSpecifications());
        }

        // Handle dimensions using ProductDimensionService
        if (productDTO.getDimensions() != null) {
            productDimensionService.saveDimensions(savedProduct.getId(), productDTO.getDimensions());
        }

        return mapToDTO(savedProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return mapToDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(String keyword, String sortBy, String sortDirection, int page, int size) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection != null ? sortDirection : "ASC");
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, !Objects.equals(sortBy, "") ? sortBy : "id"));

        if (keyword.isEmpty()) {
            return productRepository.findAll(pageable).map(this::mapToDTO);
        }

        return productRepository.findByKeyword(keyword, pageable).map(this::mapToDTO);
    }

    @Override
    public Page<ProductDTO> searchProducts(Long categoryId, String brand, Double minPrice, Double maxPrice, Pageable pageable) {
        return productRepository.searchProducts(categoryId, brand, minPrice, maxPrice, pageable)
                .map(this::mapToDTO);
    }

    @Override
    @Transactional
    public void updateStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        int newStock = product.getStockQuantity() + quantity;
        if (newStock < 0) {
            throw new IllegalArgumentException("Insufficient stock");
        }

        product.setStockQuantity(newStock);
        productRepository.save(product);
    }

    private ProductDTO mapToDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setLongDescription(product.getLongDescription());
        dto.setPrice(product.getPrice());
        dto.setMainImage(product.getMainImage());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setBrand(product.getBrand());
        dto.setCategoryId(product.getCategory().getId());
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

        dto.setColors(product.getColors().stream()
                .map(color -> {
                    ColorDTO colorDTO = new ColorDTO();
                    colorDTO.setId(color.getId());
                    colorDTO.setName(color.getName());
                    colorDTO.setValue(color.getValue());
                    colorDTO.setHex(color.getHex());
                    return colorDTO;
                })
                .collect(Collectors.toList()));

        dto.setSizes(new ArrayList<>(product.getSizes()));

        dto.setSpecifications(product.getSpecifications().stream()
                .map(spec -> {
                    ProductSpecificationDTO specDTO = new ProductSpecificationDTO();
                    specDTO.setId(spec.getId());
                    specDTO.setKey(spec.getKey());
                    specDTO.setValue(spec.getValue());
                    return specDTO;
                })
                .collect(Collectors.toList()));

        dto.setDimensions(product.getDimensions().stream()
                .map(dim -> {
                    ProductDimensionDTO dimDTO = new ProductDimensionDTO();
                    dimDTO.setId(dim.getId());
                    dimDTO.setKey(dim.getKey());
                    dimDTO.setValue(dim.getValue());
                    return dimDTO;
                })
                .collect(Collectors.toList()));

        return dto;
    }
} 