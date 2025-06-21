package com.example.order_service.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO dùng để nhận dữ liệu khi cập nhật User (người dùng).
 */
@Getter
public class UpdateUserRequest {
    private String userName;
    private String password;

}
