package com.example.order_service.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.order_service.dto.request.CreateRentalRequest;
import com.example.order_service.dto.response.GetRentalResponse;
import com.example.order_service.service.RentalService;

import lombok.AllArgsConstructor;

/**
 * Controller xử lý các API liên quan đến việc thuê xe (rental).
 * Bao gồm tạo yêu cầu thuê, lấy danh sách và chi tiết thuê.
 */
@RestController
@RequestMapping("api/v1/rentals")
@AllArgsConstructor
public class RentalController {

    RentalService rentalService;

    /**
     * API lấy danh sách tất cả các lần thuê xe.
     *
     * @return danh sách thông tin các lần thuê
     */
    @GetMapping
    public List<GetRentalResponse> getRentals() {
        return rentalService.getRentals();
    }

    /**
     * API lấy thông tin chi tiết của một lần thuê xe theo ID.
     *
     * @param id ID của rental cần lấy
     * @return thông tin chi tiết rental
     */
    @GetMapping("{id}")
    public GetRentalResponse getRental(@PathVariable int id) {
        return rentalService.getRental(id);
    }

    /**
     * API để tạo một yêu cầu thuê xe mới.
     *
     * @param request thông tin yêu cầu thuê xe
     */
    @PostMapping
    public void rentCar(@RequestBody CreateRentalRequest request) {
        rentalService.rentCar(request);
    }
}
