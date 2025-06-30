package com.example.order_service.service;

import com.example.order_service.dto.request.CreateCarRequest;
import com.example.order_service.dto.request.UpdateCarRequest;
import com.example.order_service.dto.response.CreateCarResponse;
import com.example.order_service.dto.response.GetCarResponse;
import com.example.order_service.entities.Brand;
import com.example.order_service.entities.Car;
import com.example.order_service.repository.BrandRepository;
import com.example.order_service.repository.CarRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Triển khai các nghiệp vụ liên quan đến Car (xe).
 * Bao gồm tạo, lấy, cập nhật và xoá thông tin xe.
 */
@Service
@AllArgsConstructor
public class CarServiceImpl implements CarService {

    private static final Logger LOG = LogManager.getLogger(CarServiceImpl.class);

    @Autowired
    RedisService redisService;

    @Autowired
    CarRepository carRepository;

    @Autowired
    BrandRepository brandRepository;

    /**
     * Tạo mới một xe nếu model và brand chưa trùng nhau.
     *
     * @param request thông tin xe cần tạo
     * @return phản hồi chứa thông tin xe vừa được tạo
     */
    @Override
    public CreateCarResponse createCar(CreateCarRequest request) {
        if (carRepository.existsByModel(request.getModel()) && brandRepository.existsById(request.getBrandId())) {
            LOG.error("Tên xe và thương hiệu đã tồn tại");
            throw new RuntimeException("Car with this model and brand already exists");
        }

        Brand brand = brandRepository.findById(request.getBrandId()).orElseThrow(() -> {
            LOG.error("Không tìm thấy thương hiệu");
            return new RuntimeException("Brand not found");
        });

        Car car = new Car();
        car.setModel(request.getModel());
        car.setPrice(request.getPrice());
        car.setAmount(request.getAmount());
        car.setBrand(brand);

        carRepository.save(car);
        CreateCarResponse response = new CreateCarResponse(
                car.getModel(),
                car.getPrice(),
                car.getAmount(),
                car.getBrand().getName(),
                car.getBrand().getCountry()
        );
        LOG.info("Thêm xe thành công");
        redisService.delete("cars:all");
        return response;
    }

    /**
     * Lấy thông tin chi tiết của một xe theo ID.
     *
     * @param id ID xe cần lấy
     * @return thông tin chi tiết xe
     */
    @Override
    public GetCarResponse getCar(int id) {
        Car car = carRepository.findById(id).orElseThrow(() -> {
            LOG.error("Không tìm thấy xe có ID: {}", id);
            return new RuntimeException("Car not found");
        });

        String cacheKey = "car:" + id;
        long start = System.currentTimeMillis();

        GetCarResponse cached = redisService.get(cacheKey, GetCarResponse.class);
        if(cached != null) {
            long end = System.currentTimeMillis();
            LOG.info("Lấy xe có ID: {} từ redis thành công", id);
            LOG.info("Duration (cache): {} ms ", end - start);
            return cached;
        }


        GetCarResponse response =  new GetCarResponse(
                car.getId(),
                car.getModel(),
                car.getPrice(),
                car.getAmount(),
                car.getBrand().getName()
        );
        long end = System.currentTimeMillis();
        LOG.info("Xem thông tin xe với ID: {} thành công", id);
        LOG.info("Duration (cache): {} ms ", end - start);
        redisService.save(cacheKey, response, 10, TimeUnit.HOURS);
        return response;
    }

    /**
     * Lấy danh sách tất cả các xe trong hệ thống.
     *
     * @return danh sách thông tin xe
     */
    @Override
    public List<GetCarResponse> getCars() {

        String cacheKey = "cars:all";
        long start = System.currentTimeMillis();

        List<GetCarResponse> cached = redisService.get(cacheKey, new TypeReference<List<GetCarResponse>>() {
        });
        if(cached != null) {
            long end = System.currentTimeMillis();
            LOG.info("Lấy thông tin danh sách người dùng thành công từ Redis");
            LOG.info("Duration (cache): {} ms", end - start);
            return cached;
        }
        List<Car> cars = carRepository.findAll();
        List<GetCarResponse> getCarResponses = new ArrayList<>();

        for (Car car : cars) {
            getCarResponses.add(new GetCarResponse(
                    car.getId(),
                    car.getModel(),
                    car.getPrice(),
                    car.getAmount(),
                    car.getBrand().getName()
            ));
        }

        long end = System.currentTimeMillis();
        LOG.info("Xem thông tin danh sách xe thành công từ DB");
        LOG.info("Duration (no cache): {} ms", end - start);
        redisService.save(cacheKey, getCarResponses, 10, TimeUnit.HOURS);
        return getCarResponses;
    }

    /**
     * Xoá một xe theo ID nếu tồn tại.
     *
     * @param id ID xe cần xoá
     */
    @Override
    public void deleteCar(int id) {
        Car car = carRepository.findById(id).orElseThrow(() -> {
            LOG.error("Không tìm thấy xe có ID: {} để xoá", id);
            return new RuntimeException("Car not found");
        });

        carRepository.delete(car);
        LOG.info("Xoá xe có ID: {} thành công", id);
        redisService.delete("cars:all");
        redisService.delete("car:" + id);
    }

    /**
     * Cập nhật thông tin xe theo ID.
     * Chỉ cập nhật các trường không rỗng hoặc khác 0.
     *
     * @param request thông tin cập nhật
     * @param id      ID xe cần cập nhật
     */
    @Override
    public void updateCar(UpdateCarRequest request, int id) {
        Car car = carRepository.findById(id).orElseThrow(() -> {
            LOG.error("Không tìm thấy xe có ID: {} để cập nhật", id);
            return new RuntimeException("Car not found");
        });

        if (request.getModel() != null)
            car.setModel(request.getModel());
        if (request.getPrice() != 0)
            car.setPrice(request.getPrice());
        if (request.getAmount() != 0)
            car.setAmount(request.getAmount());

        carRepository.save(car);
        LOG.info("Cập nhật xe có ID: {} thành công", id);
        redisService.delete("cars:all");
        redisService.delete("car:" + id);
    }
}
