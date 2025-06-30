package com.example.order_service.service;

import com.example.order_service.dto.request.CreateRentalRequest;
import com.example.order_service.dto.response.GetRentalResponse;
import com.example.order_service.entities.Car;
import com.example.order_service.entities.Rental;
import com.example.order_service.entities.User;
import com.example.order_service.repository.CarRepository;
import com.example.order_service.repository.RentalRepository;
import com.example.order_service.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Triển khai các nghiệp vụ liên quan đến việc thuê xe (rental).
 * Gồm tạo yêu cầu thuê, lấy chi tiết và danh sách các lượt thuê.
 */
@Service
@AllArgsConstructor
public class RentalServiceImpl implements RentalService {

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private RedisService redisService;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final RentalRepository rentalRepository;

    /**
     * Lấy thông tin chi tiết một lượt thuê xe theo ID.
     *
     * @param id ID của lượt thuê
     * @return DTO chứa thông tin chi tiết lượt thuê
     */
    @Override
    public GetRentalResponse getRental(int id) {
        Rental rental = rentalRepository.findById(id).orElseThrow(() -> {
            LOG.error("Không tìm thấy mục cho thuê có ID: {}", id);
            return new RuntimeException("Rental not found");
        });

        String cacheKey = "rental:" + id;
        long start = System.currentTimeMillis();

        GetRentalResponse cached = redisService.get(cacheKey, GetRentalResponse.class);
        if(cached != null) {
            long end = System.currentTimeMillis();
            LOG.info("Lấy lượt thuê có ID: {} từ redis thành công", id);
            LOG.info("Duration (cache): {} ms ", end - start);
            return cached;
        }
        LOG.info("Lấy thông tin lượt thuê ID: {} thành công", id);

        GetRentalResponse response =  new GetRentalResponse(
                rental.getId(),
                rental.getRentalDate(),
                rental.getRentalDays(),
                rental.getReturnDate(),
                rental.getUser().getUserName(),
                rental.getCar().getModel(),
                rental.getTotalPrice()
        );
        long end = System.currentTimeMillis();
        LOG.info("Xem thông tin lượt thuê với ID: {} thành công từ DB", id);
        LOG.info("Duration (cache): {} ms ", end - start);
        redisService.save(cacheKey, response, 10, TimeUnit.HOURS);
        return response;
    }

    /**
     * Lấy danh sách tất cả lượt thuê xe trong hệ thống.
     *
     * @return danh sách DTO các lượt thuê
     */
    @Override
    public List<GetRentalResponse> getRentals() {
        String cacheKey = "rentals:all";
        long start = System.currentTimeMillis();

        List<GetRentalResponse> cached = redisService.get(cacheKey, new TypeReference<List<GetRentalResponse>>() {
        });
        if(cached != null) {
            long end = System.currentTimeMillis();
            LOG.info("Lấy lượt thuê xe từ redis thành công");
            LOG.info("Duration (cache): {} ms ", end - start);
            return cached;
        }
        List<Rental> rentals = rentalRepository.findAll();
        List<GetRentalResponse> getRentalResponses = new ArrayList<>();

        for (Rental rental : rentals) {
            getRentalResponses.add(new GetRentalResponse(
                    rental.getId(),
                    rental.getRentalDate(),
                    rental.getRentalDays(),
                    rental.getReturnDate(),
                    rental.getUser().getUserName(),
                    rental.getCar().getModel(),
                    rental.getTotalPrice()
            ));
        }

        LOG.info("Lấy danh sách các mục cho thuê thành công");
        long end = System.currentTimeMillis();
        LOG.info("Duration (cache): {} ms ", end - start);
        redisService.save(cacheKey, getRentalResponses, 10, TimeUnit.HOURS);
        return getRentalResponses;
    }

    /**
     * Thực hiện thao tác thuê xe:
     * - Kiểm tra người dùng và xe tồn tại
     * - Kiểm tra xe còn hàng
     * - Tạo bản ghi thuê và cập nhật số lượng xe
     *
     * @param request yêu cầu thuê xe
     */
    @Override
    public void rentCar(CreateRentalRequest request) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> {
                    LOG.error("Không tìm thấy người dùng muốn thuê xe có tên: {}", userName);
                    return new RuntimeException("User not found");
                });

        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> {
                    LOG.error("Không tìm thấy xe muốn được thuê có ID: {}", request.getCarId());
                    return new RuntimeException("Car not found");
                });

        if (car.getAmount() <= 0) {
            LOG.error("Loại xe này hiện tại không có sẵn!");
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

        // Giảm số lượng xe còn lại sau khi thuê
        car.setAmount(car.getAmount() - 1);
        carRepository.save(car);
        LOG.info("Thuê xe thành công cho người dùng ID: {}, xe ID: {}", user.getId(), car.getId());
        redisService.delete("rentals:all");
    }
}
