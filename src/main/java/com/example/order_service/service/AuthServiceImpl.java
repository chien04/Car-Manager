package com.example.order_service.service;

//import com.example.order_service.configuration.JWTProvider;
import com.example.order_service.configuration.JWTConstant;
import com.example.order_service.dto.request.LoginRequest;
import com.example.order_service.dto.request.SignupRequest;
import com.example.order_service.dto.response.AuthResponse;
import com.example.order_service.entities.USER_ROLE;
import com.example.order_service.entities.User;
import com.example.order_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.experimental.NonFinal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{

    @NonFinal
    protected static final String SIGNER_KEY = JWTConstant.SECRET_KEY;
    @Autowired
    private UserRepository userRepository;
    private static final Logger LOG = LogManager.getLogger();
    @Override
    public AuthResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByUserName(request.getUserName());
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Tài khoản không tồn tại");
        }
        User user = userOptional.get();
        if (!Objects.equals(user.getPassword(), request.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }
        String token = generateToken(user);
        return AuthResponse.builder()
                .jwt(token)
                .message("Login success")
                .role(user.getRole())
                .build();
    }
    @Override
    public AuthResponse signup(SignupRequest request) {
        if(userRepository.existsByUserName(request.getUserName()))
            throw new RuntimeException("Tài khoản đã tồn tại");

        User user = new User();
        user.setUserName(request.getUserName());
        user.setPassword(request.getPassword());
        user.setRole(USER_ROLE.ROLE_CUSTOMER);
        userRepository.save(user);

        String token = generateToken(user);
        return AuthResponse.builder()
                .jwt(token)
                .role(user.getRole())
                .role(USER_ROLE.ROLE_CUSTOMER)
                .message("Signup success")
                .build();
    }


    private String generateToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName())
                .issuer("localhost:8080")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", user.getRole().name())
                .build();

        Payload payload= new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
             LOG.error("Cannot create token");
            throw new RuntimeException(e);
        }
    }
}
