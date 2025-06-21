package com.example.order_service.service;

import com.example.order_service.dto.request.CreateCarRequest;
import com.example.order_service.dto.request.UpdateCarRequest;
import com.example.order_service.dto.response.CreateCarResponse;
import com.example.order_service.dto.response.GetCarResponse;
import com.example.order_service.entities.Brand;
import com.example.order_service.entities.Car;
import com.example.order_service.repository.BrandRepository;
import com.example.order_service.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    @Autowired
    CarRepository carRepository;

    @Autowired
    BrandRepository brandRepository;
    @Override
    public CreateCarResponse createCar(CreateCarRequest request) {
        if(carRepository.existsByModel(request.getModel()) && brandRepository.existsById(request.getBrandId())) {
            throw new RuntimeException("Car with this model and brand already exists");
        }
        Brand brand = brandRepository.findById(request.getBrandId()).orElseThrow(() -> new RuntimeException("Brand not found"));
        Car car = new Car();

        car.setModel(request.getModel());
        car.setPrice(request.getPrice());
        car.setAmount(request.getAmount() + car.getAmount());
        car.setBrand(brand);

        carRepository.save(car);
        return new CreateCarResponse(car.getModel(), car.getPrice(), car.getAmount(),
                                        car.getBrand().getName(), car.getBrand().getCountry());
    }

    @Override
    public GetCarResponse getCar(int id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found"));
        return new GetCarResponse(car.getId(), car.getModel(), car.getPrice(), car.getAmount(), car.getBrand().getName());
    }

    @Override
    public List<GetCarResponse> getCars() {
        List<Car> cars = carRepository.findAll();

        List<GetCarResponse> getCarResponses = new ArrayList<>();
        for (Car car: cars) {
            getCarResponses.add(new GetCarResponse(car.getId(), car.getModel(),
                    car.getPrice(), car.getAmount(), car.getBrand().getName()));
        }
        return getCarResponses;
    }

    @Override
    public void deleteCar(int id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found"));


        carRepository.delete(car);
    }

    @Override
    public void updateCar(UpdateCarRequest request, int id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new RuntimeException("Car not found"));
        if(request.getModel() != null)
            car.setModel(request.getModel());
        if(request.getPrice() != 0)
            car.setPrice(request.getPrice());
        if(request.getAmount() != 0)
            car.setAmount(request.getAmount());
        carRepository.save(car);
    }
}
