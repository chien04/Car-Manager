package com.example.order_service.controller;

import com.example.order_service.dto.request.CreateUserRequest;
import com.example.order_service.dto.request.UpdateUserRequest;
import com.example.order_service.dto.response.CreateUserResponse;
import com.example.order_service.dto.response.GetUserResponse;
import com.example.order_service.entities.User;
import com.example.order_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    @GetMapping
    public List<GetUserResponse> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public GetUserResponse getUser(@PathVariable int id) {
        return userService.getUser(id);
    }

    @PutMapping("/{id}")
    public void updateUser(@RequestBody UpdateUserRequest userRequest,
                            @PathVariable int id) {
        userService.updateUser(userRequest, id);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}
