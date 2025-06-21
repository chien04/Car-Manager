package com.example.order_service.dto.request;

import lombok.Getter;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;

/**
 * DTO dùng để nhận dữ liệu khi tạo mới một mục cho thuê.
 */
@Getter
public class CreateRentalRequest {
    private int userId;
    private int carId;
    private LocalDate rentalDate;
    private int rentalDays;
}
