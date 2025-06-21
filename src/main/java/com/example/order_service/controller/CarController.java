package com.example.order_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.order_service.dto.request.CreateCarRequest;
import com.example.order_service.dto.request.UpdateCarRequest;
import com.example.order_service.dto.response.CreateCarResponse;
import com.example.order_service.dto.response.GetCarResponse;
import com.example.order_service.service.CarService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/cars")
@AllArgsConstructor
public class CarController {

    @Autowired
    CarService carService;

    @PostMapping
    public CreateCarResponse createCar(@RequestBody CreateCarRequest carRequest) {
        return carService.createCar(carRequest);
    }

    @GetMapping("/{id}")
    public GetCarResponse getCar(@PathVariable int id) {
        return carService.getCar(id);
    }

    @GetMapping
    public List<GetCarResponse> getCars() {
        return carService.getCars();
    }

    @PutMapping("/{id}")
    public void updateCar(@RequestBody UpdateCarRequest request,
                          @PathVariable int id) {
        carService.updateCar(request, id);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable int id){
        carService.deleteCar(id);
    }
}
