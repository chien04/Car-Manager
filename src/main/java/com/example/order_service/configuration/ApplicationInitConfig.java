package com.example.order_service.configuration;

import com.example.order_service.entities.USER_ROLE;
import com.example.order_service.entities.User;
import com.example.order_service.repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationInitConfig {

    private static final Logger LOG = LogManager.getLogger();
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if(userRepository.findByUserName("admin").isEmpty()) {
                User user = User.builder()
                        .userName("admin")
                        .password("admin123")
                        .role(USER_ROLE.ROLE_ADMIN)
                        .build();
                userRepository.save(user);
                LOG.warn("admin has been created with default password admin123");
            }
        };
    }
}
