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

/**
 * Controller quản lý các API liên quan đến Brand (hãng xe).
 * Cung cấp các chức năng tạo, cập nhật, xoá và truy xuất thông tin brand.
 */
@RestController
@RequestMapping("api/v1/brands")
@AllArgsConstructor
public class BrandController {

    @Autowired
    BrandService brandService;

    /**
     * API tạo mới một brand.
     *
     * @param brandRequest thông tin brand cần tạo
     * @return response chứa brand vừa được tạo
     */
    @PostMapping
    public CreateBrandResponse createBrand(@RequestBody CreateBrandRequest brandRequest) {
        return brandService.createBrand(brandRequest);
    }

    /**
     * API cập nhật brand theo ID.
     *
     * @param brandRequest thông tin mới cần cập nhật
     * @param id ID của brand cần cập nhật
     * @return đối tượng Brand sau khi cập nhật
     */
    @PutMapping("/{id}")
    public Brand updateBrand(@RequestBody UpdateBrandRequest brandRequest,
                             @PathVariable int id){
        return brandService.updateBrand(brandRequest, id);
    }

    /**
     * API lấy danh sách tất cả các brand.
     *
     * @return danh sách các brand (được map thành DTO)
     */
    @GetMapping
    public List<GetBrandResponse> getBrands() {
        return brandService.getBrands();
    }

    /**
     * API lấy thông tin chi tiết của một brand theo ID.
     *
     * @param id ID của brand cần lấy
     * @return thông tin chi tiết brand dưới dạng DTO
     */
    @GetMapping("/{id}")
    public GetBrandResponse getBrand(@PathVariable int id) {
        return brandService.getBrand(id);
    }

    /**
     * API xoá brand theo ID.
     *
     * @param id ID của brand cần xoá
     */
    @DeleteMapping("/{id}")
    public void deleteBrand(@PathVariable int id){
        brandService.deleteBrand(id);
    }
}
