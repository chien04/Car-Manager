package com.example.order_service.dto.request;

import lombok.Getter;

/**
 * DTO dùng để nhận dữ liệu khi tạo mới một Brand (hãng xe).
 */
@Getter
public class CreateBrandRequest {
    private String name;
    private String country;

}
