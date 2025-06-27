package com.example.order_service.dto.response;

import com.example.order_service.entities.USER_ROLE;
import lombok.Builder;

@Builder
public record AuthResponse(String jwt, String message, USER_ROLE role) {

}
