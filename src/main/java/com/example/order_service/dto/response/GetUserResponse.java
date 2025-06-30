package com.example.order_service.dto.response;

import com.example.order_service.entities.Rental;
import com.example.order_service.entities.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO phản hồi khi lấy thông tin của một User (người dùng).
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetUserResponse {
    private int id;
    private String username;
    private List<RentalDTO> rentalDTOS;
    private USER_ROLE role;

    // constructor, getters, setters
}
