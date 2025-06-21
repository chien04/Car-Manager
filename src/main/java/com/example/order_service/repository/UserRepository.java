package com.example.order_service.repository;

import com.example.order_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository cho thực thể User.
 * Cung cấp sẵn các phương thức CRUD từ JpaRepository và thêm các truy vấn tùy chỉnh.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByUserName(String userName);
}
