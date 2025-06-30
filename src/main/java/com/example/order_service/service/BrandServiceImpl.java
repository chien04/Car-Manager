package com.example.order_service.service;

import com.example.order_service.dto.request.CreateBrandRequest;
import com.example.order_service.dto.request.UpdateBrandRequest;
import com.example.order_service.dto.response.CreateBrandResponse;
import com.example.order_service.dto.response.GetBrandResponse;
import com.example.order_service.entities.Brand;
import com.example.order_service.repository.BrandRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Triển khai các nghiệp vụ xử lý liên quan đến Brand (hãng xe).
 * Bao gồm tạo, cập nhật, truy xuất, xoá brand và log hoạt động tương ứng.
 */
@Service
@AllArgsConstructor
public class BrandServiceImpl implements BrandService {

    private static final Logger LOG = LogManager.getLogger(BrandServiceImpl.class);

    @Autowired
    private RedisService redisService;

    @Autowired
    private BrandRepository brandRepository;

    /**
     * Tạo một brand mới nếu chưa tồn tại theo tên và quốc gia.
     *
     * @param request yêu cầu tạo brand
     * @return phản hồi chứa thông tin brand mới
     */
    @Override
    public CreateBrandResponse createBrand(CreateBrandRequest request) {
        if (brandRepository.existsByName(request.getName()) && brandRepository.existsByCountry(request.getCountry())) {
            LOG.error("Tên thương hiệu {} của quốc gia {} đã tồn tại", request.getName(), request.getCountry());
            throw new RuntimeException("Tên thương hiệu và quốc gia đã tồn tại");
        }

        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setCountry(request.getCountry());

        brandRepository.save(brand);
        LOG.info("Thêm thương hiệu {} của quốc gia {} thành công", request.getName(), request.getCountry());
        CreateBrandResponse response = new CreateBrandResponse(brand.getName(), brand.getCountry());
        redisService.delete("brands:all");
        return response;
    }

    /**
     * Cập nhật thông tin của một brand dựa theo ID.
     *
     * @param request yêu cầu cập nhật brand
     * @param id      ID của brand cần cập nhật
     * @return brand sau khi được cập nhật
     */
    @Override
    public Brand updateBrand(UpdateBrandRequest request, int id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> {
            LOG.error("Không tìm thấy thương hiệu có ID: {} để cập nhật", id);
            return new RuntimeException("Không tìm thấy thương hiệu để cập nhật");
        });

        brand.setName(request.getName());
        brand.setCountry(request.getCountry());

        brandRepository.save(brand);
        LOG.info("Cập nhật thương hiệu thành công");
        redisService.delete("brands:all");
        redisService.delete("brand" + id);
        return brand;
    }

    /**
     * Truy xuất thông tin chi tiết của một brand theo ID.
     *
     * @param id ID của brand
     * @return đối tượng phản hồi chứa thông tin brand
     */
    @Override
    public GetBrandResponse getBrand(int id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> {
            LOG.error("Không tìm thấy thương hiệu có ID: {}", id);
            return new RuntimeException("Không tìm thấy thương hiệu");
        });

        String cacheKey = "brand:" + id;
        long start = System.currentTimeMillis();

        GetBrandResponse cached = redisService.get(cacheKey, GetBrandResponse.class);
        if(cached != null) {
            long end = System.currentTimeMillis();
            LOG.info("Lấy thông tin thương hiệu có ID: {} thành công từ Redis", id);
            LOG.info("Duration (cache): {} ms", end - start);
            return cached;
        }

        LOG.info("Lấy thông tin thương hiệu thành công");
        GetBrandResponse response = new GetBrandResponse(brand.getId(), brand.getName(), brand.getCountry());
        redisService.save(cacheKey, response, 10, TimeUnit.HOURS);
        return response;
    }

    /**
     * Truy xuất danh sách tất cả các brand.
     *
     * @return danh sách phản hồi chứa thông tin brand
     */
    @Override
    public List<GetBrandResponse> getBrands() {
        String cacheKey = "brands:all";
        long start = System.currentTimeMillis();

        List<GetBrandResponse> cached = redisService.get(cacheKey, new TypeReference<List<GetBrandResponse>>() {
        });
        if(cached != null) {
            long end = System.currentTimeMillis();
            LOG.info("Lấy thông tin danh sách thương hiệu từ Redis thành công");
            LOG.info("Duration (cache): {} ms ", end - start);
        }
        List<GetBrandResponse> getBrandResponses = new ArrayList<>();
        List<Brand> brands = brandRepository.findAll();
        for (Brand brand : brands) {
            getBrandResponses.add(new GetBrandResponse(brand.getId(), brand.getName(), brand.getCountry()));
        }
        LOG.info("Lấy thông tin danh sách các thương hiệu thành công từ DB");
        long end = System.currentTimeMillis();
        LOG.info("Duration (no cache): {} ms", end - start);
        redisService.save(cacheKey, getBrandResponses, 10, TimeUnit.HOURS);
        return getBrandResponses;
    }

    /**
     * Xoá một brand theo ID nếu tồn tại.
     *
     * @param id ID của brand cần xoá
     */
    @Override
    public void deleteBrand(int id) {
        Brand brand = brandRepository.findById(id).orElseThrow(() -> {
            LOG.error("Không tìm thấy thương hiệu có ID: {} để xoá", id);
            return new RuntimeException("Không tìm thấy thương hiệu để xoá");
        });
        brandRepository.delete(brand);
        LOG.info("Xoá thương hiệu có ID: {} thành công", id);
        redisService.delete("brand:" + id);
    }
}
