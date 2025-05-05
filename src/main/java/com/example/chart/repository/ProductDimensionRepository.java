package com.example.chart.repository;

import com.example.chart.models.ProductDimension;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDimensionRepository extends JpaRepository<ProductDimension, Long> {
    List<ProductDimension> findByProductId(Long productId);
    
    @Modifying
    @Query("DELETE FROM ProductDimension pd WHERE pd.productId = :productId")
    void deleteByProductId(@Param("productId") Long productId);
} 