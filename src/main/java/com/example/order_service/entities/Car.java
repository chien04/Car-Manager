package com.example.order_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity đại diện cho bảng Car (xe).
 * Lưu trữ thông tin về xe như model, giá, số lượng và liên kết với hãng xe (Brand).
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {

    /**
     * ID tự động tăng của xe.
     */
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private int id;
    private String model;
    private int price;
    private int amount;

    /**
     * Danh sách các lượt thuê xe (rental) có liên quan đến xe này.
     * Một xe có thể được thuê nhiều lần (OneToMany).
     */
    @OneToMany(mappedBy = "car")
    private List<Rental> rentals;

    /**
     * Hãng xe (Brand) mà chiếc xe này thuộc về.
     * Nhiều xe có thể cùng một hãng (ManyToOne).
     */
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;


}
