package com.example.order_service.service;

import com.example.order_service.dto.request.CreateBrandRequest;
import com.example.order_service.dto.request.CreateCarRequest;
import com.example.order_service.dto.request.UpdateBrandRequest;
import com.example.order_service.dto.response.CreateBrandResponse;
import com.example.order_service.dto.response.CreateCarResponse;
import com.example.order_service.dto.response.GetBrandResponse;
import com.example.order_service.entities.Brand;

import java.util.List;

public interface BrandService {
    CreateBrandResponse createBrand(CreateBrandRequest request);

    Brand updateBrand(UpdateBrandRequest request, int id);

    GetBrandResponse getBrand(int id);

    List<GetBrandResponse> getBrands();

    void deleteBrand(int id);
}
