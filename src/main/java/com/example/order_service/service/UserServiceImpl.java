package com.example.order_service.service;

import com.example.order_service.dto.request.CreateUserRequest;
import com.example.order_service.dto.request.UpdateUserRequest;
import com.example.order_service.dto.response.CreateUserResponse;
import com.example.order_service.dto.response.GetUserResponse;
import com.example.order_service.dto.response.RentalDTO;
import com.example.order_service.entities.Rental;
import com.example.order_service.entities.User;
import com.example.order_service.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private static final Logger LOG = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;


    public CreateUserResponse createUser(CreateUserRequest userRequest) {

        if(userRepository.existsByUserName(userRequest.getUserName())) {
            LOG.error("Tên người dùng đã tồn tại");
            throw new RuntimeException("Tên người dùng đã tồn tại");
        }
        User user = new User();
        user.setUserName(userRequest.getUserName());
        user.setPassword(userRequest.getPassword());
        userRepository.save(user);
        LOG.info("Tạo người dùng thành công");
        return new CreateUserResponse(user.getUserName());
    }

    @Override
    public List<GetUserResponse> getUsers() {

        List<User> users = userRepository.findAll();
        List<GetUserResponse> userResponses = new ArrayList<>();

        for (User user: users){
            List<RentalDTO> rentalDTOS = new ArrayList<>();
            for (Rental rental: user.getRentals()){
                rentalDTOS.add(new RentalDTO(rental.getId(),
                        rental.getCar().getModel(),
                        rental.getRentalDate(),
                        rental.getRentalDays(),
                        rental.getReturnDate(),
                        rental.getTotalPrice()));
            }
            userResponses.add(new GetUserResponse(user.getId(), user.getUserName(), rentalDTOS));
        }
        LOG.info("Lấy danh sách người dùng thành công");
        return userResponses;
    }

    @Override
    public GetUserResponse getUser(int id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            LOG.error("Không tồn tại người dùng có ID: {}", id);
            return new RuntimeException("Không ồn tại người dùng");
        });

        List<RentalDTO> rentalDTOS = new ArrayList<>();
        for (Rental rental: user.getRentals()){
            rentalDTOS.add(new RentalDTO(rental.getId(),
                    rental.getCar().getModel(),
                    rental.getRentalDate(),
                    rental.getRentalDays(),
                    rental.getReturnDate(),
                    rental.getTotalPrice()));
        }
        LOG.info("Lấy người dùng có ID: {} thành công", id);
        return new GetUserResponse(user.getId(), user.getUserName(), rentalDTOS);
    }

    @Override
    public void updateUser(UpdateUserRequest request, int id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> {
                    LOG.error("Không tìm thấy người dùng có ID: {}", id);
                    return new RuntimeException("Không tìm thấy người dùng");
                }
        );
        user.setUserName(request.getUserName());
        user.setPassword(request.getPassword());

        userRepository.save(user);
        LOG.info("Cập nhật người dùng có ID: {} thành công", id);
    }

    @Override
    public void deleteUser(int id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> {
                    LOG.error("Không tìm thấy người dùng có ID: {}", id);
                    return new RuntimeException("Không tìm thấy người dùng");
                }
        );

        userRepository.delete(user);
        LOG.info("Xoá thành công người dùng có ID: {}", id);
    }


}
