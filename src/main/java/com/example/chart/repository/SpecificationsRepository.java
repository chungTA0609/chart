package com.example.chart.repository;

import com.example.chart.models.Specifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecificationsRepository extends JpaRepository<Specifications, Long> {
}
