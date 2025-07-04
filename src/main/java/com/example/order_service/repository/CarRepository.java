package com.example.order_service.repository;

import com.example.order_service.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository cho thực thể Car.
 * Cung cấp sẵn các phương thức CRUD từ JpaRepository và thêm các truy vấn tùy chỉnh.
 */
@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    boolean existsByModel(String model);
}
