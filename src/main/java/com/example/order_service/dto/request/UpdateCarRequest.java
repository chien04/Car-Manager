package com.example.order_service.dto.request;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO dùng để nhận dữ liệu khi cập nhật Car (xe).
 */
@Getter
@Setter
public class UpdateCarRequest {
    private String model;
    private int price;
    private int amount;
}
