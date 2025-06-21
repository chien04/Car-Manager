package com.example.order_service.dto.response;

import lombok.Getter;

import java.time.LocalDate;

public record RentalDTO(
        int id,
        String carName,
        LocalDate rentalDate,
        int rentalDays,
        LocalDate returnDate,
        double totalPrice
) {}
