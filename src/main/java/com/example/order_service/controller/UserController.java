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

/**
 * Controller quản lý các API liên quan đến User (người dùng).
 * Bao gồm các chức năng: tạo, lấy danh sách, lấy chi tiết, cập nhật và xoá người dùng.
 */
@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * API tạo người dùng mới.
     *
     * @param userRequest thông tin người dùng cần tạo
     * @return thông tin người dùng sau khi tạo thành công
     */
    @PostMapping("")
    public CreateUserResponse createUser(@RequestBody CreateUserRequest userRequest) {
        return userService.createUser(userRequest);
    }

    /**
     * API lấy danh sách tất cả người dùng.
     *
     * @return danh sách người dùng
     */
    @GetMapping
    public List<GetUserResponse> getUsers() {
        return userService.getUsers();
    }

    /**
     * API lấy thông tin chi tiết người dùng theo ID.
     *
     * @param id ID người dùng cần lấy
     * @return thông tin người dùng tương ứng
     */
    @GetMapping("/{id}")
    public GetUserResponse getUser(@PathVariable int id) {
        return userService.getUser(id);
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

    /**
     * API xoá người dùng theo ID.
     *
     * @param id ID người dùng cần xoá
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}
