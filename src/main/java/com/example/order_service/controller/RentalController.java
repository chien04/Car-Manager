package com.example.order_service.controller;

import com.example.order_service.dto.request.CreateRentalRequest;
import com.example.order_service.service.RentalService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/rentals")
@AllArgsConstructor
public class RentalController {

    RentalService rentalService;
    @PostMapping
    public void rentCar(@RequestBody CreateRentalRequest request) {
        rentalService.rentCar(request);
    }
}
