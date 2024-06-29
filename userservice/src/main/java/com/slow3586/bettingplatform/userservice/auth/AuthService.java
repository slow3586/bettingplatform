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
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public String login(LoginRequest loginRequest) {
        final AuthEntity authEntity = authRepository
            .findByLogin(loginRequest.getLogin());

        if (authEntity == null) {
            throw new IllegalArgumentException("Invalid login");
        }

        if (!passwordEncoder.matches(
            loginRequest.getPassword(),
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

    public String register(RegisterRequest registerRequest) {
        if (authRepository.existsByLogin(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email address already in use");
        }

        final String login = authRepository.save(
            AuthEntity.builder()
                .login(registerRequest.getEmail())
                .role("user")
                .status("new")
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build()).getLogin();

        customerRepository.save(
            CustomerEntity.builder()
                .login(login)
                .name("name")
                .balance(0)
                .status("normal")
                .hasPremium(false)
                .build());

        return login;
    }

    public String checkToken(String token) {
        final Claims claims;
        try {
            claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            if (claims.getSubject() == null || claims.getExpiration() == null) {
                throw new IllegalArgumentException("Invalid claims");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid token");
        }

        if (Instant.now().isAfter(claims.getExpiration().toInstant())) {
            throw new IllegalArgumentException("Token expired");
        }

        final AuthEntity authEntity = authRepository.findByLogin(claims.getSubject());

        if (authEntity == null) {
            throw new IllegalArgumentException("Unknown user");
        }

        return authEntity.getLogin();
    }
}
