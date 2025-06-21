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

/**
 * Controller quản lý các API liên quan đến Car (xe).
 * Gồm các chức năng: thêm, cập nhật, xoá, lấy danh sách và chi tiết xe.
 */
@RestController
@RequestMapping("api/v1/cars")
@AllArgsConstructor
public class CarController {

    @Autowired
    CarService carService;

    /**
     * API tạo xe mới.
     *
     * @param carRequest thông tin xe cần tạo
     * @return thông tin xe sau khi tạo thành công
     */
    @PostMapping
    public CreateCarResponse createCar(@RequestBody CreateCarRequest carRequest) {
        return carService.createCar(carRequest);
    }

    /**
     * API lấy thông tin chi tiết của một xe theo ID.
     *
     * @param id ID của xe cần lấy
     * @return thông tin chi tiết xe
     */
    @GetMapping("/{id}")
    public GetCarResponse getCar(@PathVariable int id) {
        return carService.getCar(id);
    }

    /**
     * API lấy danh sách tất cả các xe.
     *
     * @return danh sách xe
     */
    @GetMapping
    public List<GetCarResponse> getCars() {
        return carService.getCars();
    }

    /**
     * API cập nhật thông tin một xe theo ID.
     *
     * @param request thông tin mới của xe
     * @param id ID của xe cần cập nhật
     */
    @PutMapping("/{id}")
    public void updateCar(@RequestBody UpdateCarRequest request,
                          @PathVariable int id) {
        carService.updateCar(request, id);
    }

    /**
     * API xoá một xe theo ID.
     *
     * @param id ID của xe cần xoá
     */
    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable int id){
        carService.deleteCar(id);
    }
}
