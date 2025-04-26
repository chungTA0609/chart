package com.example.chart.repository;

import com.example.chart.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            @Param("name") String name,
            @Param("description") String description,
            Pageable pageable
    );

    Page<Product> findByCategoriesId(Long categoryId, Pageable pageable);

    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<Product> findByBrand(String brand, Pageable pageable);

    Page<Product> findByRatingGreaterThanEqual(Double rating, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%) AND " +
            "(:categoryId IS NULL OR EXISTS (SELECT c FROM p.categories c WHERE c.id = :categoryId)) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
            "(:brand IS NULL OR p.brand = :brand) AND " +
            "(:minRating IS NULL OR p.rating >= :minRating)")
    Page<Product> searchAndFilter(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("brand") String brand,
            @Param("minRating") Double minRating,
            Pageable pageable
    );

    @Modifying
    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity - :quantity " +
            "WHERE p.id = :id AND p.stockQuantity >= :quantity")
    int decreaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);

    @Modifying
    @Query("UPDATE Product p SET p.stockQuantity = p.stockQuantity + :quantity " +
            "WHERE p.id = :id")
    int increaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);
}