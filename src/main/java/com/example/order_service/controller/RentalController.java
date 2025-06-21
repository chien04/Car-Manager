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

@RestController
@RequestMapping("api/v1/rentals")
@AllArgsConstructor
public class RentalController {

    RentalService rentalService;

    @GetMapping
    public List<GetRentalResponse> getRentals() {
        return rentalService.getRentals();
    }

    @GetMapping("{id}")
    public GetRentalResponse getRental(@PathVariable int id) {
        return rentalService.getRental(id);
    }

    @PostMapping
    public void rentCar(@RequestBody CreateRentalRequest request) {
        rentalService.rentCar(request);
    }
}
