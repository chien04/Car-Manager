package com.example.order_service.dto.response;

import java.time.LocalDate;

public record GetRentalResponse(int id, LocalDate rentalDate, int rentalDays, LocalDate returnDate, String username, String carName, double totalPrice) {

}
