package com.example.order_service.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class Config implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("https://car-manager-front-end.vercel.app/") // 🎯 CHỈ định đúng domain frontend
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true); // Nếu có dùng cookie hoặc auth
    }
}
