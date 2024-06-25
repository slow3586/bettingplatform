package com.slow3586.bettingplatform.userservice.auth;

import com.slow3586.bettingplatform.api.userservice.dto.LoginRequest;
import com.slow3586.bettingplatform.api.userservice.dto.RegisterRequest;
import com.slow3586.bettingplatform.userservice.customer.CustomerEntity;
import com.slow3586.bettingplatform.userservice.customer.CustomerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class AuthService {
    AuthRepository authRepository;
    CustomerRepository customerRepository;
    AuthProperties authProperties;
    PasswordEncoder passwordEncoder;
    SecretKey secretKey;

    public Mono<String> login(LoginRequest loginRequest) {
        return Mono.just(loginRequest)
            .publishOn(Schedulers.boundedElastic())
            .mapNotNull(request -> authRepository.findByLogin(request.getLogin()))
            .filter(authEntity -> passwordEncoder.matches(
                loginRequest.getPassword(),
                authEntity.getPassword()))
            .flatMap(authEntity -> this.generateToken(authEntity.getUserId()))
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Incorrect username or password")));
    }

    protected Mono<UUID> register(RegisterRequest registerRequest) {
        return Mono.just(registerRequest)
            .publishOn(Schedulers.boundedElastic())
            .filter(request -> !authRepository.existsByLogin(request.getEmail()))
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Email is already taken")))
            .map((request) ->
                authRepository.save(
                    AuthEntity.builder()
                        .login(request.getEmail())
                        .email(registerRequest.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .build()
                ).getUserId())
            .onErrorStop()
            .map((userId) ->
                customerRepository.save(
                    CustomerEntity.builder()
                        .userId(userId)
                        .build()
                ).getUserId())
            .onErrorContinue((e, userId) ->
                authRepository.deleteByUserId((UUID) userId));
    }

    public Mono<UUID> token(String token) {
        return Mono.just(token)
            .publishOn(Schedulers.boundedElastic())
            .flatMap(this::getTokenSubject)
            .map(UUID::fromString)
            .filter(authRepository::existsByUserId);
    }

    protected Mono<String> generateToken(UUID id) {
        return Mono.just(
            Jwts.builder()
                .subject(id.toString())
                .expiration(Date.from(
                    Instant.now().plus(
                        Duration.ofMinutes(
                            authProperties.getExpirationMinutes()))))
                .signWith(secretKey)
                .compact());
    }

    protected Mono<String> getTokenSubject(String token) {
        return Mono.just(token)
            .map(t -> Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload())
            .filter(claims -> Instant.now().isBefore(claims.getExpiration().toInstant()))
            .map(Claims::getSubject);
    }
}
