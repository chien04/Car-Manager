package com.example.order_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;

/**
 * Entity đại diện cho bảng Rental (lượt thuê xe).
 * Lưu thông tin về thời gian thuê, giá, người thuê và xe đã thuê.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rental {

    /**
     * ID tự động tăng cho mỗi lượt thuê.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private LocalDate rentalDate;
    private int rentalDays;
    private LocalDate returnDate;
    private double totalPrice;

    /**
     * Người dùng đã thuê xe này.
     * Một người có thể thuê nhiều lượt (ManyToOne).
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Xe được thuê.
     * Một xe có thể được thuê nhiều lần (ManyToOne).
     */
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

}
