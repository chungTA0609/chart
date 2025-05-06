package com.example.chart.services.impl;

import com.example.chart.dto.products.ProductSpecificationDTO;
import com.example.chart.models.ProductSpecification;
import com.example.chart.repository.ProductSpecificationRepository;
import com.example.chart.services.ProductSpecificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSpecificationServiceImpl implements ProductSpecificationService {
    private final ProductSpecificationRepository productSpecificationRepository;

    @Override
    @Transactional
    public List<ProductSpecificationDTO> saveSpecifications(Long productId, List<ProductSpecificationDTO> specifications) {
        // Delete existing specifications for the product
        deleteSpecificationsByProductId(productId);

        // Create and save new specifications
        List<ProductSpecification> savedSpecs = specifications.stream()
            .map(specDTO -> {
                ProductSpecification spec = new ProductSpecification();
                spec.setKey(specDTO.getKey());
                spec.setValue(specDTO.getValue());
                spec.setProductId(productId);
                return productSpecificationRepository.save(spec);
            })
            .collect(Collectors.toList());

        // Map saved specifications back to DTOs
        return savedSpecs.stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteSpecificationsByProductId(Long productId) {
        productSpecificationRepository.deleteByProductId(productId);
    }

    @Override
    public List<ProductSpecificationDTO> getSpecificationsByProductId(Long productId) {
        return productSpecificationRepository.findByProductId(productId).stream()
            .map(this::mapToDTO)
            .collect(Collectors.toList());
    }

    private ProductSpecificationDTO mapToDTO(ProductSpecification spec) {
        ProductSpecificationDTO dto = new ProductSpecificationDTO();
        dto.setId(spec.getId());
        dto.setKey(spec.getKey());
        dto.setValue(spec.getValue());
        dto.setProductId(spec.getProductId());
        return dto;
    }
} 