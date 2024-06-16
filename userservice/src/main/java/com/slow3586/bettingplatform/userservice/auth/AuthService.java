package com.slow3586.bettingplatform.userservice.auth;

import com.slow3586.bettingplatform.api.userservice.LoginRequest;
import com.slow3586.bettingplatform.api.userservice.RegisterRequest;
import com.slow3586.bettingplatform.userservice.customer.CustomerEntity;
import com.slow3586.bettingplatform.userservice.customer.CustomerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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

    public Mono<String> login(LoginRequest loginRequest) {
        return Mono.just(loginRequest)
            .publishOn(Schedulers.boundedElastic())
            .mapNotNull(request -> authRepository.findByLogin(request.getLogin()))
            .filter(authEntity -> passwordEncoder.matches(
                loginRequest.getPassword(),
                authEntity.getPassword()))
            .flatMap(authEntity -> this.generateToken(authEntity.getId()))
            .switchIfEmpty(Mono.error(new IllegalArgumentException("Incorrect username or password")));
    }

    public Mono<UUID> register(RegisterRequest registerRequest) {
        return Mono.just(registerRequest)
            .publishOn(Schedulers.boundedElastic())
            .handle((request, sink) -> {
                final UUID userId = UUID.randomUUID();
                authRepository.save(
                    AuthEntity.builder()
                        .userId(userId)
                        .login(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .build());
                try {
                    customerRepository.save(
                        CustomerEntity.builder()
                            .userId(userId)
                            .email(registerRequest.getEmail())
                            .name("name")
                            .balance(0)
                            .build());
                } catch (Exception e) {
                    authRepository.deleteByUserId(userId);
                    sink.error(e);
                    return;
                }
                sink.next(userId);
            });
    }

    public Mono<UUID> token(String token) {
        return Mono.just(token)
            .flatMap(this::getTokenSubject)
            .map(UUID::fromString)
            .filter(authRepository::existsById);
    }

    protected Mono<String> generateToken(UUID id) {
        return Mono.just(
            Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(id.toString())
                .expiration(Date.from(Instant.now().plus(Duration.ofMinutes(authProperties.getExpirationMinutes()))))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(authProperties.getToken())))
                .compact());
    }

    protected Mono<String> getTokenSubject(String token) {
        return Mono.just(token)
            .map(t -> Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(authProperties.getToken())))
                .build()
                .parseSignedClaims(token)
                .getPayload())
            .filter(claims -> Instant.now().isBefore(claims.getExpiration().toInstant()))
            .map(Claims::getSubject);
    }
}
