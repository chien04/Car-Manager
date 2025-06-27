package com.example.order_service.dto.request;

import lombok.Data;
import lombok.Getter;

@Getter
public class SignupRequest {
    private String userName;
    private String password;
}
