package com.example.order_service.service;

import com.example.order_service.dto.request.CreateUserRequest;
import com.example.order_service.dto.request.UpdateUserRequest;
import com.example.order_service.dto.response.CreateUserResponse;
import com.example.order_service.dto.response.GetUserResponse;
import com.example.order_service.dto.response.RentalDTO;
import com.example.order_service.entities.Rental;
import com.example.order_service.entities.USER_ROLE;
import com.example.order_service.entities.User;
import com.example.order_service.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Triển khai các nghiệp vụ xử lý liên quan đến người dùng (User).
 * Gồm tạo, lấy, cập nhật, xoá người dùng và hiển thị các lượt thuê xe của họ.
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private CacheManager cacheManager;

    /**
     * Tạo mới một người dùng nếu chưa tồn tại username.
     *
     * @param userRequest thông tin người dùng cần tạo
     * @return phản hồi chứa tên người dùng mới tạo
     */
    public CreateUserResponse createUser(CreateUserRequest userRequest) {
        if (userRepository.existsByUserName(userRequest.getUserName())) {
            LOG.error("Tên người dùng đã tồn tại");
            throw new RuntimeException("Tên người dùng đã tồn tại");
        }

        User user = new User();
        user.setUserName(userRequest.getUserName());
        user.setPassword(userRequest.getPassword());
        user.setRole(USER_ROLE.ROLE_CUSTOMER);
        userRepository.save(user);
        LOG.info("Tạo người dùng thành công");
        redisService.delete("users:all");
        return new CreateUserResponse(user.getUserName());
    }

    /**
     * Lấy danh sách tất cả người dùng, kèm theo danh sách các lượt thuê của họ.
     *
     * @return danh sách phản hồi người dùng và lượt thuê
     */
    @Override
    public List<GetUserResponse> getUsers() {
        String cacheKey = "users:all";
        long start = System.currentTimeMillis();
        List<GetUserResponse> cached = redisService.get(cacheKey, new TypeReference<List<GetUserResponse>>() {
        });
        if(cached != null) {
            long end = System.currentTimeMillis();
            LOG.info("Lấy danh sách người dùng từ redis cache");
            LOG.info("Duration (cache): {} ms", end - start);
            return cached;
        }

        List<User> users = userRepository.findAll();
        List<GetUserResponse> userResponses = new ArrayList<>();
        for (User user : users) {
            List<RentalDTO> rentalDTOS = new ArrayList<>();
            for (Rental rental : user.getRentals()) {
                rentalDTOS.add(new RentalDTO(
                        rental.getId(),
                        rental.getCar().getModel(),
                        rental.getRentalDate(),
                        rental.getRentalDays(),
                        rental.getReturnDate(),
                        rental.getTotalPrice()
                ));
            }
            userResponses.add(new GetUserResponse(user.getId(), user.getUserName(), rentalDTOS, user.getRole()));
        }
        LOG.info("Lấy danh sách người dùng từ DB");
        long end = System.currentTimeMillis();
        LOG.info("Duration (no cache): {} ms", end - start);
        redisService.save(cacheKey, userResponses, 10, TimeUnit.MINUTES);
        LOG.info("Lưu danh sách người dùng vào Redis cache");
        return userResponses;
    }

    /**
     * Lấy thông tin chi tiết người dùng theo ID, kèm danh sách lượt thuê của họ.
     *
     * @param id ID người dùng
     * @return phản hồi thông tin người dùng và các lượt thuê
     */
    @Override
    public GetUserResponse getUser(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            LOG.error("Không tồn tại người dùng có ID: {}", id);
            return new RuntimeException("Không tồn tại người dùng");
        });
        long start = System.currentTimeMillis();
        String keyCache = "user:" + id;
        GetUserResponse cached = redisService.get(keyCache, GetUserResponse.class);
        if(cached != null) {
            long end = System.currentTimeMillis();
            LOG.info("Lấy người dùng có ID: {} từ Redis", id);
            LOG.info("Duration (cache): {} ms", end - start);
            return cached;
        }
        List<RentalDTO> rentalDTOS = new ArrayList<>();
        for (Rental rental : user.getRentals()) {
            rentalDTOS.add(new RentalDTO(
                    rental.getId(),
                    rental.getCar().getModel(),
                    rental.getRentalDate(),
                    rental.getRentalDays(),
                    rental.getReturnDate(),
                    rental.getTotalPrice()
            ));
        }
        long end = System.currentTimeMillis();
        LOG.info("Lấy người dùng có ID: {} thành công từ DB", id);
        LOG.info("Duration (no cache): {} ms", end - start);
        redisService.save(keyCache, new GetUserResponse(), 10, TimeUnit.MINUTES);
        return new GetUserResponse(user.getId(), user.getUserName(), rentalDTOS, user.getRole());
    }

    @Override
    public GetUserResponse getUser(String userName) {
        User user = userRepository.findByUserName(userName).orElseThrow(() -> {
            LOG.error("Không tồn tại người dùng có userName: {}", userName);
            return new RuntimeException("Không tồn tại người dùng");
        });

        List<RentalDTO> rentalDTOS = new ArrayList<>();
        for (Rental rental : user.getRentals()) {
            rentalDTOS.add(new RentalDTO(
                    rental.getId(),
                    rental.getCar().getModel(),
                    rental.getRentalDate(),
                    rental.getRentalDays(),
                    rental.getReturnDate(),
                    rental.getTotalPrice()
            ));
        }

        LOG.info("Lấy người dùng có userName: {} thành công", userName);
        return new GetUserResponse(user.getId(), user.getUserName(), rentalDTOS, user.getRole());
    }

    /**
     * Cập nhật thông tin người dùng theo ID.
     *
     * @param request thông tin cần cập nhật
     * @param id      ID người dùng cần cập nhật
     */

    @Override
    public void updateUser(UpdateUserRequest request, int id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            LOG.error("Không tìm thấy người dùng có ID: {}", id);
            return new RuntimeException("Không tìm thấy người dùng");
        });

        user.setUserName(request.getUserName());
        user.setPassword(request.getPassword());

        userRepository.save(user);
        LOG.info("Cập nhật người dùng có ID: {} thành công", id);

        redisService.delete("users:all");
        redisService.delete("user:" + id);
    }

    /**
     * Xoá người dùng theo ID nếu tồn tại.
     *
     * @param id ID người dùng cần xoá
     */
    @Override
    public void deleteUser(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            LOG.error("Không tìm thấy người dùng có ID: {}", id);
            return new RuntimeException("Không tìm thấy người dùng");
        });

        userRepository.delete(user);
        LOG.info("Xoá thành công người dùng có ID: {}", id);
        redisService.delete("users:all");
        redisService.delete("user:" + id);
    }

}
