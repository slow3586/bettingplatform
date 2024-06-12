package com.slow3586.bettingplaftorm.userservice.user;

import com.slow3586.bettingplaftorm.api.LoginRequest;
import com.slow3586.bettingplaftorm.api.RegisterRequest;
import com.slow3586.bettingplaftorm.userservice.jwt.JwtComponent;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
        return Mono.just(loginRequest)
            .publishOn(Schedulers.boundedElastic())
            .map(request -> userRepository.findById(UUID.fromString(request.getLogin())))
            .flatMap(Mono::justOrEmpty)
            .filter(userEntity -> userEntity.getPassword().equals(loginRequest.getPassword()))
            .flatMap(userEntity -> jwtComponent.generateToken(userEntity.getId()));
    }

    public Mono<String> register(RegisterRequest registerRequest) {
        return Mono.just(registerRequest)
            .publishOn(Schedulers.boundedElastic())
            .map(request -> userRepository.save(
                UserEntity.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build()
            )).flatMap(userEntity -> jwtComponent.generateToken(userEntity.getId()));
    }

    public Mono<UUID> token(String token) {
        return Mono.just(token)
            .flatMap(jwtComponent::getTokenSubject)
            .map(UUID::fromString)
            .filter(userRepository::existsById);
    }
}
