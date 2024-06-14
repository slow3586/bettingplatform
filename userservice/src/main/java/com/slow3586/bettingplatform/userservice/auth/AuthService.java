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

import java.math.BigInteger;
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
            .map(request -> authRepository.findById(UUID.fromString(request.getLogin())))
            .flatMap(Mono::justOrEmpty)
            .filter(authEntity -> authEntity.getPassword().equals(loginRequest.getPassword()))
            .flatMap(authEntity -> this.generateToken(authEntity.getId()));
    }

    public Mono<UUID> register(RegisterRequest registerRequest) {
        return Mono.just(registerRequest)
            .publishOn(Schedulers.boundedElastic())
            .map(request -> {
                final UUID id = authRepository.save(
                    AuthEntity.builder()
                        .login(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .build()
                ).getId();
                try {
                    customerRepository.save(
                        CustomerEntity.builder()
                            .id(id)
                            .email(registerRequest.getEmail())
                            .name("name")
                            .money(BigInteger.ZERO)
                            .build());
                } catch (Exception e) {
                    authRepository.deleteById(id);
                }
                return id;
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
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * authProperties.getExpirationMinutes()))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(authProperties.getToken())))
                .compact());
    }

    protected Mono<String> getTokenSubject(String token) {
        return Mono.just(token)
            .map(t -> Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(authProperties.getToken())))
                .requireIssuer("v1")
                .build()
                .parseSignedClaims(token)
                .getPayload())
            .filter(claims -> claims.getExpiration().before(new Date()))
            .map(Claims::getSubject);
    }
}
