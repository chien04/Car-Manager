package com.example.order_service.controller;

import com.example.order_service.dto.request.UpdateUserRequest;
import com.example.order_service.dto.response.GetUserResponse;
import com.example.order_service.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
@AllArgsConstructor
public class UserController {

    UserService userService;

    @GetMapping()
    public GetUserResponse getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return userService.getUser(userName);
    }

    /**
     * API cập nhật thông tin người dùng theo ID.
     *
     * @param userRequest thông tin cần cập nhật
     * @param id ID người dùng cần cập nhật
     */
    @PutMapping("/{id}")
    public void updateUser(@RequestBody UpdateUserRequest userRequest,
                           @PathVariable int id) {
        userService.updateUser(userRequest, id);
    }
}
