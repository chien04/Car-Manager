package com.example.order_service.repository;

import com.example.order_service.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    boolean existsByName(String name);

    boolean existsByCountry(String country);
}
