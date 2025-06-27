package com.example.order_service.service;

import com.example.order_service.dto.request.LoginRequest;
import com.example.order_service.dto.request.SignupRequest;
import com.example.order_service.dto.response.AuthResponse;

public interface AuthService {
        AuthResponse login(LoginRequest request);
        AuthResponse signup(SignupRequest request);
}
