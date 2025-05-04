package com.example.chart.services.impl;

import com.example.chart.dto.*;
import com.example.chart.models.*;
import com.example.chart.repository.*;
import com.example.chart.services.ProductService;
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

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final ColorRepository colorRepository;
    private final ProductSpecificationRepository productSpecificationRepository;
    private final ProductDimensionRepository productDimensionRepository;

    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

        Product product = new Product();
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

        Product savedProduct = productRepository.save(product);

        // Set images
        if (productDTO.getImages() != null) {
            for (ProductImageDTO imageDTO : productDTO.getImages()) {
                ProductImage image = new ProductImage();
                image.setImageUrl(imageDTO.getImageUrl());
                image.setMain(imageDTO.isMain());
                image.setProductId(savedProduct.getId());
                productImageRepository.save(image);
            }
        }

        // Set colors
        if (productDTO.getColors() != null) {
            for (ColorDTO colorDTO : productDTO.getColors()) {
                Color color = new Color();
                color.setName(colorDTO.getName());
                color.setValue(colorDTO.getValue());
                color.setHex(colorDTO.getHex());
                color.setProductId(savedProduct.getId());
                colorRepository.save(color);
            }
        }

        // Set sizes
        if (productDTO.getSizes() != null) {
            product.setSizes(productDTO.getSizes());
        }

        // Set specifications
        if (productDTO.getSpecifications() != null) {
            for (ProductSpecificationDTO specDTO : productDTO.getSpecifications()) {
                ProductSpecification spec = new ProductSpecification();
                spec.setKey(specDTO.getKey());
                spec.setValue(specDTO.getValue());
                spec.setProductId(savedProduct.getId());
                productSpecificationRepository.save(spec);
            }
        }

        // Set dimensions
        if (productDTO.getDimensions() != null) {
            for (ProductDimensionDTO dimDTO : productDTO.getDimensions()) {
                ProductDimension dim = new ProductDimension();
                dim.setKey(dimDTO.getKey());
                dim.setValue(dimDTO.getValue());
                dim.setProductId(savedProduct.getId());
                productDimensionRepository.save(dim);
            }
        }

        return mapToDTO(savedProduct);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));

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

        // Update images
        if (productDTO.getImages() != null) {
            product.getImages().clear();
            for (ProductImageDTO imageDTO : productDTO.getImages()) {
                ProductImage image = new ProductImage();
                image.setImageUrl(imageDTO.getImageUrl());
                image.setMain(imageDTO.isMain());
                image.setProductId(product.getId());
                productImageRepository.save(image);
            }
        }

        // Update colors
        if (productDTO.getColors() != null) {
            product.getColors().clear();
            for (ColorDTO colorDTO : productDTO.getColors()) {
                Color color = new Color();
                color.setName(colorDTO.getName());
                color.setValue(colorDTO.getValue());
                color.setHex(colorDTO.getHex());
                color.setProductId(product.getId());
                colorRepository.save(color);
            }
        }

        // Update sizes
        if (productDTO.getSizes() != null) {
            product.getSizes().clear();
            product.getSizes().addAll(productDTO.getSizes());
        }

        // Update specifications
        if (productDTO.getSpecifications() != null) {
            product.getSpecifications().clear();
            for (ProductSpecificationDTO specDTO : productDTO.getSpecifications()) {
                ProductSpecification spec = new ProductSpecification();
                spec.setKey(specDTO.getKey());
                spec.setValue(specDTO.getValue());
                spec.setProductId(product.getId());
                productSpecificationRepository.save(spec);
            }
        }

        // Update dimensions
        if (productDTO.getDimensions() != null) {
            product.getDimensions().clear();
            for (ProductDimensionDTO dimDTO : productDTO.getDimensions()) {
                ProductDimension dim = new ProductDimension();
                dim.setKey(dimDTO.getKey());
                dim.setValue(dimDTO.getValue());
                dim.setProductId(product.getId());
                productDimensionRepository.save(dim);
            }
        }

        Product updatedProduct = productRepository.save(product);
        return mapToDTO(updatedProduct);
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
                .collect(Collectors.toSet()));

        dto.setColors(product.getColors().stream()
                .map(color -> {
                    ColorDTO colorDTO = new ColorDTO();
                    colorDTO.setId(color.getId());
                    colorDTO.setName(color.getName());
                    colorDTO.setValue(color.getValue());
                    colorDTO.setHex(color.getHex());
                    return colorDTO;
                })
                .collect(Collectors.toSet()));

        dto.setSizes(product.getSizes());

        dto.setSpecifications(product.getSpecifications().stream()
                .map(spec -> {
                    ProductSpecificationDTO specDTO = new ProductSpecificationDTO();
                    specDTO.setId(spec.getId());
                    specDTO.setKey(spec.getKey());
                    specDTO.setValue(spec.getValue());
                    return specDTO;
                })
                .collect(Collectors.toSet()));

        dto.setDimensions(product.getDimensions().stream()
                .map(dim -> {
                    ProductDimensionDTO dimDTO = new ProductDimensionDTO();
                    dimDTO.setId(dim.getId());
                    dimDTO.setKey(dim.getKey());
                    dimDTO.setValue(dim.getValue());
                    return dimDTO;
                })
                .collect(Collectors.toSet()));

        return dto;
    }
} 