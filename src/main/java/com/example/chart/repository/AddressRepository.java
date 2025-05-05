package com.example.chart.repository;

import com.example.chart.models.Address;
import com.example.chart.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUserId(Long userId);
    List<Address> findByUserIdOrderByIsDefaultDesc(Long userId);
    Optional<Address> findByUserIdAndIsDefaultTrue(Long userId);
    List<Address> findByUserIdAndIsDefaultFalse(Long userId);
    long countByUserId(Long userId);
}