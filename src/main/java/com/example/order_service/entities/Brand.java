package com.example.order_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * Entity đại diện cho bảng Brand (hãng xe).
 * Lưu trữ thông tin tên hãng và quốc gia, và ánh xạ mối quan hệ với các xe thuộc hãng đó.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class Brand {

    /**
     * ID tự động tăng của hãng xe.
     */
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String country;

    /**
     * Danh sách các xe thuộc hãng này.
     * Một hãng có thể có nhiều xe (OneToMany).
     */
    @OneToMany(mappedBy = "brand")
    private List<Car> cars;
}
