package com.example.order_service.service;

import com.example.order_service.dto.request.CreateRentalRequest;
import com.example.order_service.dto.response.GetRentalResponse;
import com.example.order_service.dto.response.GetUserResponse;
import com.example.order_service.entities.Car;
import com.example.order_service.entities.Rental;
import com.example.order_service.entities.User;
import com.example.order_service.repository.CarRepository;
import com.example.order_service.repository.RentalRepository;
import com.example.order_service.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class RentalServiceImpl implements RentalService{

    private static final Logger LOG = LogManager.getLogger();
    UserRepository userRepository;
    CarRepository carRepository;
    RentalRepository rentalRepository;

    @Override
    public GetRentalResponse getRental(int id) {
        Rental rental = rentalRepository.findById(id).orElseThrow(() -> {

            return new RuntimeException("Rental not found");
        }
        );
        return new GetRentalResponse(rental.getId(),
                rental.getRentalDate(), rental.getRentalDays(),
                rental.getReturnDate(), rental.getUser().getUserName(),
                rental.getCar().getModel(), rental.getTotalPrice());
    }

    @Override
    public List<GetRentalResponse> getRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        List<GetRentalResponse> getRentalResponses = new ArrayList<>();
        for (Rental rental: rentals){
            getRentalResponses.add(new GetRentalResponse(rental.getId(),
                    rental.getRentalDate(), rental.getRentalDays(),
                    rental.getReturnDate(), rental.getUser().getUserName(),
                    rental.getCar().getModel(), rental.getTotalPrice()));
        }
        return getRentalResponses;
    }

    @Override
    public void rentCar(CreateRentalRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new RuntimeException("Car not found"));

        if(car.getAmount() < 0) {
            throw new RuntimeException("Car is not available");
        }

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setCar(car);
        rental.setRentalDate(request.getRentalDate());
        rental.setRentalDays(request.getRentalDays());
        rental.setReturnDate(request.getRentalDate().plusDays(request.getRentalDays()));
        rental.setTotalPrice(car.getPrice() * request.getRentalDays());
        rentalRepository.save(rental);

        car.setAmount(car.getAmount() - 1);
        carRepository.save(car);


    }
}
