package com.example.order_service.dto.request;

import lombok.Getter;

@Getter
public class CreateCarRequest {
    private String model;
    private int price;
    private int amount;
    private int brandId;

}
