package com.example.order_service.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO dùng để nhận dữ liệu khi tạo mới một User (người dùng).
 */
@Getter
@Setter
public class CreateUserRequest{
    private String userName;
    private String password;

}
