package com.example.order_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity đại diện cho bảng User (người dùng).
 * Lưu trữ thông tin tài khoản người dùng và các lượt thuê xe liên quan.
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * ID tự động tăng của người dùng.
     */
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private int id;
    private String userName;
    private String password;
    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;

    /**
     * Danh sách các lượt thuê xe của người dùng.
     * Một người dùng có thể có nhiều lượt thuê (OneToMany).
     */
    @OneToMany(mappedBy = "user")
    private List<Rental> rentals;
}

