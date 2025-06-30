package com.example.order_service.service;

import com.example.order_service.dto.request.CreateUserRequest;
import com.example.order_service.dto.request.UpdateUserRequest;
import com.example.order_service.dto.response.CreateUserResponse;
import com.example.order_service.dto.response.GetUserResponse;
import com.example.order_service.entities.User;

import java.util.List;

/**
 * Interface định nghĩa các chức năng xử lý nghiệp vụ liên quan đến User (người dùng).
 */
public interface UserService {
    CreateUserResponse createUser(CreateUserRequest userRequest);

    List<GetUserResponse> getUsers();

    GetUserResponse getUser(int id);

    GetUserResponse getUser(String userName);
    void updateUser(UpdateUserRequest userRequest, int id);

    void deleteUser(int id);

}
