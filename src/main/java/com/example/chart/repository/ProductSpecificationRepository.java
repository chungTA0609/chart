package com.example.chart.repository;

import com.example.chart.models.ProductSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Long> {
    List<ProductSpecification> findByProductId(Long productId);
    
    @Modifying
    @Query("DELETE FROM ProductSpecification ps WHERE ps.productId = :productId")
    void deleteByProductId(@Param("productId") Long productId);
} 