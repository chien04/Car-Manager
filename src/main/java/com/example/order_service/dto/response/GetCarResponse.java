package com.example.order_service.dto.response;

public record GetCarResponse(int id, String model, int price, int amount, String brand) {
}
