package com.example.chart.repository;

import com.example.chart.models.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {
    List<Color> findByProductId(Long productId);
    
    @Modifying
    @Query("DELETE FROM Color c WHERE c.productId = :productId")
    void deleteByProductId(@Param("productId") Long productId);
} 