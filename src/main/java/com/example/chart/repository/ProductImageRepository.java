package com.example.chart.repository;

import com.example.chart.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    List<ProductImage> findByProductId(Long productId);
    
    @Modifying
    @Query("DELETE FROM ProductImage pi WHERE pi.productId = :productId")
    void deleteByProductId(@Param("productId") Long productId);
} 