package com.example.order_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class Brand {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String country;

    @OneToMany(mappedBy = "brand")
    private List<Car> cars;
}
