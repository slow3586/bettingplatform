package com.slow3586.bettingplaftorm.userservice;

import com.slow3586.bettingplaftorm.userservice.dto.LoginRequest;
import com.slow3586.bettingplaftorm.userservice.dto.RegisterRequest;
import com.slow3586.bettingplaftorm.userservice.jwt.JwtComponent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    JwtComponent jwtComponent;
    PasswordEncoder passwordEncoder;

    public Mono<String> login(LoginRequest loginRequest) {
        return Mono.justOrEmpty(userRepository.findById(UUID.fromString(loginRequest.getLogin())))
            .single()
            .filter(userEntity -> userEntity.getPassword().equals(loginRequest.getPassword()))
            .flatMap(userEntity -> jwtComponent.generateToken(userEntity.getId()));
    }

    public Mono<String> register(RegisterRequest registerRequest) {
        return Mono.just(userRepository.save(
            UserEntity.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build()
        )).flatMap(userEntity -> jwtComponent.generateToken(userEntity.getId()));
    }

    public Mono<String> token(String token) {
        return Mono.just(token)
            .flatMap(jwtComponent::getTokenUser);
    }
}
