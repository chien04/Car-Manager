package com.example.order_service.service;

import com.example.order_service.dto.request.CreateRentalRequest;
import com.example.order_service.dto.response.GetRentalResponse;
import com.example.order_service.entities.Rental;

import java.util.List;

public interface RentalService {

    GetRentalResponse getRental(int id);

    List<GetRentalResponse> getRentals();
    void rentCar(CreateRentalRequest request);
}
