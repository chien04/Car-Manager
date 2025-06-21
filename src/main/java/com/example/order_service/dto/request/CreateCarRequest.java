package com.example.order_service.dto.request;

import lombok.Getter;

/**
 * DTO dùng để nhận dữ liệu khi tạo mới một Car (xe).
 */
@Getter
public class CreateCarRequest {
    private String model;
    private int price;
    private int amount;
    private int brandId;

}
