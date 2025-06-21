package com.example.order_service.repository;

import com.example.order_service.entities.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository cho thực thể Brand.
 * Cung cấp sẵn các phương thức CRUD từ JpaRepository và thêm các truy vấn tùy chỉnh.
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    /**
     * Kiểm tra xem đã tồn tại hãng xe với tên cho trước hay chưa.
     *
     * @param name tên của hãng xe
     * @return true nếu tên đã tồn tại, ngược lại false
     */
    boolean existsByName(String name);

    /**
     * Kiểm tra xem đã tồn tại hãng xe với quốc gia cho trước hay chưa.
     *
     * @param country tên quốc gia
     * @return true nếu có hãng xe ở quốc gia đó, ngược lại false
     */
    boolean existsByCountry(String country);
}
