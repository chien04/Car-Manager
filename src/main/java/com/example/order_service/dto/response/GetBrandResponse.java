package com.example.order_service.dto.response;

/**
 * DTO phản hồi khi lấy thông tin của một Brand (hãng xe).
 */
public record GetBrandResponse(int id, String name, String country) {

}
