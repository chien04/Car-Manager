package com.example.order_service.controller;

import com.example.order_service.dto.request.CreateBrandRequest;
import com.example.order_service.dto.request.UpdateBrandRequest;
import com.example.order_service.dto.response.CreateBrandResponse;
import com.example.order_service.dto.response.GetBrandResponse;
import com.example.order_service.entities.Brand;
import com.example.order_service.service.BrandService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/brands")
@AllArgsConstructor
public class BrandController {

    @Autowired
    BrandService brandService;

    @PostMapping
    public CreateBrandResponse createBrand(@RequestBody CreateBrandRequest brandRequest) {
        return brandService.createBrand(brandRequest);
    }

    @PutMapping("/{id}")
    public Brand updateBrand(@RequestBody UpdateBrandRequest brandRequest,
                             @PathVariable int id){
        return brandService.updateBrand(brandRequest, id);
    }

    @GetMapping
    public List<GetBrandResponse> getBrands() {
        return brandService.getBrands();
    }

    @GetMapping("/{id}")
    public GetBrandResponse getBrand(@PathVariable int id) {
        return brandService.getBrand(id);
    }

    @DeleteMapping("/{id}")
    public void deleteBrand(@PathVariable int id){
        brandService.deleteBrand(id);
    }
}
