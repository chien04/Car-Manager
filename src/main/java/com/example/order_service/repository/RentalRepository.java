package com.example.order_service.repository;

import com.example.order_service.entities.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository cho thực thể Rental.
 * Cung cấp sẵn các phương thức CRUD từ JpaRepository và thêm các truy vấn tùy chỉnh.
 */
public interface RentalRepository extends JpaRepository<Rental, Integer> {
}
