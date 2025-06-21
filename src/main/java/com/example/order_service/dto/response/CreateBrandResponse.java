package com.example.order_service.dto.response;

/**
 * DTO phản hồi khi tạo mới một Brand (hãng xe) thành công.
 */
public record CreateBrandResponse(String brand, String country) {
}
