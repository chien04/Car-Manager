package com.example.order_service.dto.request;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String userName;
    private String password;
}
