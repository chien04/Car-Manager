package com.example.order_service.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Áp dụng cho tất cả các API
                .allowedOrigins("http://localhost:5173") // React app URL
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Cho phép method
                .allowedHeaders("*"); // Cho phép tất cả header
    }
}
