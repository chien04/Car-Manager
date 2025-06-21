package com.example.order_service.dto.response;

import com.example.order_service.entities.Rental;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO phản hồi khi lấy thông tin của một User (người dùng).
 */
public record GetUserResponse(int id, String username, List<RentalDTO> rentalDTOS) {
}
