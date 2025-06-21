package com.example.order_service.dto.response;


public record CreateCarResponse(String model, int price, int amount, String brand, String country) {

}
