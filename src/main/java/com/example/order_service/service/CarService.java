package com.example.order_service.service;

import com.example.order_service.dto.request.CreateCarRequest;
import com.example.order_service.dto.request.UpdateCarRequest;
import com.example.order_service.dto.response.CreateCarResponse;
import com.example.order_service.dto.response.GetCarResponse;
import com.example.order_service.entities.Car;

import java.util.List;

public interface CarService {
    CreateCarResponse createCar(CreateCarRequest request);

    GetCarResponse getCar(int id);

    List<GetCarResponse> getCars();

    void deleteCar(int id);

    void updateCar(UpdateCarRequest request, int id);
}
