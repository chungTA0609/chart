package com.example.chart.services.impl;

import com.example.chart.dto.products.ProductDimensionDTO;
import com.example.chart.models.ProductDimension;
import com.example.chart.repository.ProductDimensionRepository;
import com.example.chart.services.ProductDimensionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductDimensionServiceImpl implements ProductDimensionService {
    private final ProductDimensionRepository productDimensionRepository;

    @Override
    @Transactional
    public List<ProductDimensionDTO> saveDimensions(Long productId, List<ProductDimensionDTO> dimensions) {
        // Delete existing dimensions for the product
        deleteDimensionsByProductId(productId);

        // Create and save new dimensions
        List<ProductDimension> savedDimensions = dimensions.stream()
            .map(dimDTO -> {
                ProductDimension dim = new ProductDimension();
                dim.setKey(dimDTO.getKey());
                dim.setValue(dimDTO.getValue());
                dim.setProductId(productId);
                return productDimensionRepository.save(dim);
            })
            .collect(Collectors.toList());

        // Map saved dimensions back to DTOs
        return savedDimensions.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteDimensionsByProductId(Long productId) {
        productDimensionRepository.deleteByProductId(productId);
    }

    @Override
    public List<ProductDimensionDTO> getDimensionsByProductId(Long productId) {
        return productDimensionRepository.findByProductId(productId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    private ProductDimensionDTO mapToDTO(ProductDimension dim) {
        ProductDimensionDTO dto = new ProductDimensionDTO();
        dto.setId(dim.getId());
        dto.setKey(dim.getKey());
        dto.setValue(dim.getValue());
        dto.setProductId(dim.getProductId());
        return dto;
    }
} 