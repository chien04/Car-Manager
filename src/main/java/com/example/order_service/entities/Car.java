package com.example.order_service.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private int id;
    private String model;
    private int price;
    private int amount;

    @OneToMany(mappedBy = "car")
    private List<Rental> rentals;
    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;


}
