package com.example.order_service.dto.request;

import lombok.Getter;

/**
 * DTO dùng để nhận dữ liệu khi cập nhật Brand (hãng xe).
 */
@Getter
public class UpdateBrandRequest {
    private String name;
    private String country;

}
