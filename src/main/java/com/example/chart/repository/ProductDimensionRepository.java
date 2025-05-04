package com.example.chart.repository;

import com.example.chart.models.ProductDimension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductDimensionRepository extends JpaRepository<ProductDimension, Long> {
} 