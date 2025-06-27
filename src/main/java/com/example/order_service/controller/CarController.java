package com.example.order_service.controller;

import com.example.order_service.dto.response.GetCarResponse;
import com.example.order_service.service.CarService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/cars")
@AllArgsConstructor
public class CarController {

    CarService carService;
    /**
     * API lấy danh sách tất cả các xe.
     *
     * @return danh sách xe
     */
    @GetMapping
    public List<GetCarResponse> getCars() {
        return carService.getCars();
    }
}
