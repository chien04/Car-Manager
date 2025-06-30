package com.example.order_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * DTO dùng để truyền dữ liệu thuê xe của người dùng.
 * Bao gồm thông tin về xe, ngày thuê, số ngày thuê và tổng chi phí.
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RentalDTO {
    private int id;
    private String carName;
    private LocalDate rentalDate;
    private int rentalDays;
    private LocalDate returnDate;
    private double totalPrice;
}
