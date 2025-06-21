package com.example.order_service.service;

import com.example.order_service.dto.request.CreateBrandRequest;
import com.example.order_service.dto.request.UpdateBrandRequest;
import com.example.order_service.dto.response.CreateBrandResponse;
import com.example.order_service.dto.response.GetBrandResponse;
import com.example.order_service.entities.Brand;
import com.example.order_service.repository.BrandRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService{
    @Autowired
    BrandRepository brandRepository;
    @Override
    public CreateBrandResponse createBrand(CreateBrandRequest request) {
        Brand brand = new Brand();

        brand.setName(request.getName());
        brand.setCountry(request.getCountry());

        brandRepository.save(brand);
        return new CreateBrandResponse(brand.getName(), brand.getCountry());
    }

    @Override
    public Brand updateBrand(UpdateBrandRequest request, int id) {
        Brand brand = brandRepository.findById(id).orElseThrow();

        brand.setName(request.getName());
        brand.setCountry(request.getCountry());

        brandRepository.save(brand);
        return brand;
    }

    @Override
    public GetBrandResponse getBrand(int id) {
        Brand brand = brandRepository.findById(id).orElseThrow();

        return new GetBrandResponse(brand.getId(), brand.getName(), brand.getCountry());
    }

    @Override
    public List<GetBrandResponse> getBrands() {
        List<GetBrandResponse> getBrandResponses = new ArrayList<>();
        List<Brand> brands = brandRepository.findAll();
        for (Brand brand : brands) {
            getBrandResponses.add(new GetBrandResponse(brand.getId(), brand.getName(), brand.getCountry()));
        }
        return getBrandResponses;
    }

    @Override
    public void deleteBrand(int id){
        Brand brand = brandRepository.findById(id).orElseThrow();
        brandRepository.delete(brand);
    }
}
