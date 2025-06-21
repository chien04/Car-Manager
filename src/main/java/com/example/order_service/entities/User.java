package com.example.order_service.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private int id;
    private String userName;
    private String password;

    @OneToMany(mappedBy = "user")
    private List<Rental> rentals;
}

