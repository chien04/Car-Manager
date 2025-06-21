package com.example.order_service.service;

import com.example.order_service.dto.request.CreateUserRequest;
import com.example.order_service.dto.request.UpdateUserRequest;
import com.example.order_service.dto.response.CreateUserResponse;
import com.example.order_service.dto.response.GetUserResponse;
import com.example.order_service.entities.User;

import java.util.List;

public interface UserService {
    CreateUserResponse createUser(CreateUserRequest userRequest);

    List<GetUserResponse> getUsers();

    GetUserResponse getUser(int id);

    void updateUser(UpdateUserRequest userRequest, int id);

    void deleteUser(int id);
}
