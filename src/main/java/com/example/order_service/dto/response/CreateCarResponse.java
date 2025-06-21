package com.example.order_service.dto.response;

/**
 * DTO phản hồi khi tạo mới một Car (xe) thành công.
 */
public record CreateCarResponse(String model, int price, int amount, String brand, String country) {

}
