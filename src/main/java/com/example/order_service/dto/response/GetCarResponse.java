package com.example.order_service.dto.response;

/**
 * DTO phản hồi khi lấy thông tin của một Car (xe).
 */
public record GetCarResponse(int id, String model, int price, int amount, String brand) {
}
