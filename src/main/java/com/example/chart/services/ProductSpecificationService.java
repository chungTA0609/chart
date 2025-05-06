package com.example.chart.services;

import com.example.chart.dto.products.ProductSpecificationDTO;
import java.util.List;

public interface ProductSpecificationService {
    List<ProductSpecificationDTO> saveSpecifications(Long productId, List<ProductSpecificationDTO> specifications);
    void deleteSpecificationsByProductId(Long productId);
    List<ProductSpecificationDTO> getSpecificationsByProductId(Long productId);
} 