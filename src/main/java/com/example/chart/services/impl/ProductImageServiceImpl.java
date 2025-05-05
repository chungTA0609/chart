package com.example.chart.services.impl;

import com.example.chart.dto.ProductImageDTO;
import com.example.chart.models.ProductImage;
import com.example.chart.repository.ProductImageRepository;
import com.example.chart.services.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductImageServiceImpl implements ProductImageService {
    private final ProductImageRepository productImageRepository;

    @Override
    @Transactional
    public List<ProductImageDTO> saveImages(Long productId, List<ProductImageDTO> images) {
        // Delete existing images for the product
        deleteImagesByProductId(productId);

        // Create and save new images
        List<ProductImage> savedImages = images.stream()
            .map(imageDTO -> {
                ProductImage image = new ProductImage();
                image.setImageUrl(imageDTO.getImageUrl());
                image.setMain(imageDTO.isMain());
                image.setProductId(productId);
                return productImageRepository.save(image);
            })
            .collect(Collectors.toList());

        // Map saved images back to DTOs
        return savedImages.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteImagesByProductId(Long productId) {
        productImageRepository.deleteByProductId(productId);
    }

    @Override
    public List<ProductImageDTO> getImagesByProductId(Long productId) {
        return productImageRepository.findByProductId(productId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    private ProductImageDTO mapToDTO(ProductImage image) {
        ProductImageDTO dto = new ProductImageDTO();
        dto.setId(image.getId());
        dto.setImageUrl(image.getImageUrl());
        dto.setMain(image.isMain());
        dto.setProductId(image.getProductId());
        return dto;
    }
} 