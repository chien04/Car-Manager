package com.example.order_service.service;

import com.example.order_service.dto.request.CreateBrandRequest;
import com.example.order_service.dto.request.UpdateBrandRequest;
import com.example.order_service.dto.response.CreateBrandResponse;
import com.example.order_service.dto.response.GetBrandResponse;
import com.example.order_service.entities.Brand;
import com.example.order_service.repository.BrandRepository;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService{

    private static final Logger LOG = LogManager.getLogger(BrandServiceImpl.class);
    @Autowired
    BrandRepository brandRepository;
    @Override
    public CreateBrandResponse createBrand(CreateBrandRequest request) {
        if(brandRepository.existsByName(request.getName()) && brandRepository.existsByCountry(request.getCountry())){
            LOG.error("Tên thương hiệu {} của quốc gia {} đã tồn tại", request.getName(), request.getCountry());
            throw new RuntimeException("Tên thương hiệu và quốc gia đã tồn tại");
        }
        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setCountry(request.getCountry());

        brandRepository.save(brand);
        LOG.info("Thêm thương hiệu {} của quốc gia {} thành công", request.getName(), request.getCountry());
        return new CreateBrandResponse(brand.getName(), brand.getCountry());
    }

    @Override
    public Brand updateBrand(UpdateBrandRequest request, int id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() ->
        {
            LOG.error("Không tìm thấy thương hiệu có ID: {} để cập nhật", id);
            return new RuntimeException("Không tìm thấy thương hiệu để cập nhật");
        });

        brand.setName(request.getName());
        brand.setCountry(request.getCountry());

        brandRepository.save(brand);
        LOG.info("Cập nhật thương hiệu thành công");
        return brand;
    }

    @Override
    public GetBrandResponse getBrand(int id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> {
            LOG.error("Không tìm thấy thương hiệu có ID: {}", id);
            return new RuntimeException("Không tìm thấy thương hiệu");
        });
        LOG.info("Lấy thông tin thương hiệu thành công");
        return new GetBrandResponse(brand.getId(), brand.getName(), brand.getCountry());
    }

    @Override
    public List<GetBrandResponse> getBrands() {
        List<GetBrandResponse> getBrandResponses = new ArrayList<>();
        List<Brand> brands = brandRepository.findAll();
        for (Brand brand : brands) {
            getBrandResponses.add(new GetBrandResponse(brand.getId(), brand.getName(), brand.getCountry()));
        }
        LOG.info("Lấy thông tin danh sách các thương hiệu thành công");
        return getBrandResponses;
    }

    @Override
    public void deleteBrand(int id){
        Brand brand = brandRepository.findById(id).orElseThrow(() ->
        {
            LOG.error("Không tìm thấy thương hiệu có ID: {} để xoá", id);
            return new RuntimeException("Không tìm thấy thương hiệu để xoá");
        });
        brandRepository.delete(brand);
        LOG.info("Xoá thương hiệu có ID: {} thành công", id);
    }
}
