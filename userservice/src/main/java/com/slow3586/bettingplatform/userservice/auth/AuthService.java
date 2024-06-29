package com.slow3586.bettingplatform.userservice.auth;

import com.slow3586.bettingplatform.api.userservice.dto.AuthLoginRequest;
import com.slow3586.bettingplatform.api.userservice.dto.AuthRegisterRequest;
import com.slow3586.bettingplatform.userservice.customer.CustomerEntity;
import com.slow3586.bettingplatform.userservice.customer.CustomerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class AuthService {
    AuthRepository authRepository;
    CustomerRepository customerRepository;
    PasswordEncoder passwordEncoder;
    SecretKey secretKey;
    @NonFinal
    @Value("${auth.token.minutes:600}")
    int tokenMinutes;

    public String login(AuthLoginRequest authLoginRequest) {
        final AuthEntity authEntity = authRepository
            .findByName(authLoginRequest.getLogin())
            .orElseThrow(() -> new IllegalArgumentException("Invalid login"));

        if (!passwordEncoder.matches(
            authLoginRequest.getPassword(),
            authEntity.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }

        return Jwts.builder()
            .subject(authEntity.getLogin())
            .expiration(Date.from(
                Instant.now().plus(
                    Duration.ofMinutes(60))))
            .signWith(secretKey)
            .compact();
    }

    @Transactional
    @KafkaListener(topics = "auth.register.request")
    public void register(AuthRegisterRequest authRegisterRequest) {
        if (authRepository.existsByName(authRegisterRequest.getEmail())) {
            throw new IllegalArgumentException("Email address already in use");
        }

        final String login = authRepository.save(
            AuthEntity.builder()
                .login(authRegisterRequest.getEmail())
                .role("user")
                .status("new")
                .password(passwordEncoder.encode(authRegisterRequest.getPassword()))
                .build()).getLogin();

        customerRepository.save(
            CustomerEntity.builder()
                .login(login)
                .name("name")
                .balance(0)
                .status("normal")
                .hasPremium(false)
                .build());
    }

    public String checkToken(String token) {
        final String subject;
        final Instant expirationDate;
        try {
            final Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            if (claims.getSubject() == null || claims.getExpiration() == null) {
                throw new IllegalArgumentException("Invalid claims");
            }

            subject = claims.getSubject();
            expirationDate = claims.getExpiration().toInstant();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token");
        }

        if (Instant.now().isAfter(expirationDate)) {
            throw new IllegalArgumentException("Token expired");
        }

        if (!authRepository.existsByName(subject)) {
            throw new IllegalArgumentException("Unknown user");
        }

        return subject;
    }
}
