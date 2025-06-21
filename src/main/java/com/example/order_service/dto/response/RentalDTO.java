package com.example.order_service.dto.response;

import lombok.Getter;

import java.time.LocalDate;

/**
 * DTO dùng để truyền dữ liệu thuê xe của người dùng.
 * Bao gồm thông tin về xe, ngày thuê, số ngày thuê và tổng chi phí.
 *
 * @param id          ID của bản ghi thuê xe
 * @param carName     Tên xe được thuê
 * @param rentalDate  Ngày bắt đầu thuê xe
 * @param rentalDays  Số ngày thuê xe
 * @param returnDate  Ngày trả xe
 * @param totalPrice  Tổng chi phí thuê xe
 */
public record RentalDTO(
        int id,
        String carName,
        LocalDate rentalDate,
        int rentalDays,
        LocalDate returnDate,
        double totalPrice
) {}
