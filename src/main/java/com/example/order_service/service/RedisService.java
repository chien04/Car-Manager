package com.example.order_service.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Lấy 1 entity từ Redis cache
     */
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        return objectMapper.convertValue(value, clazz);
    }

    /**
     * Lấy danh sách (hoặc bất kỳ kiểu phức tạp nào) từ Redis
     */
    public <T> T get(String key, TypeReference<T> typeRef) {
        Object value = redisTemplate.opsForValue().get(key);
        return objectMapper.convertValue(value, typeRef);
    }

    /**
     * Lưu 1 entity vào cache với TTL
     */
    public void save(String key, Object value, long ttl, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, ttl, unit);
    }

    /**
     * Xoá cache
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
