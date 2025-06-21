package com.example.order_service.dto.response;

import com.example.order_service.entities.Rental;

import java.time.LocalDate;
import java.util.List;

public record GetUserResponse(int id, String username, List<RentalDTO> rentalDTOS) {
}
