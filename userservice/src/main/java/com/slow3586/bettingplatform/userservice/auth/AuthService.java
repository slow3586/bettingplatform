package com.slow3586.bettingplatform.userservice.auth;

import com.slow3586.bettingplatform.api.userservice.dto.AuthDto;
import com.slow3586.bettingplatform.api.userservice.dto.LoginRequest;
import com.slow3586.bettingplatform.api.userservice.dto.RegisterRequest;
import com.slow3586.bettingplatform.userservice.customer.CustomerEntity;
import com.slow3586.bettingplatform.userservice.customer.CustomerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@SendTo("auth.response")
@KafkaListener(topics = "auth.request", errorHandler = "replyingKafkaTemplateErrorHandler")
public class AuthService {
    AuthRepository authRepository;
    CustomerRepository customerRepository;
    AuthProperties authProperties;
    PasswordEncoder passwordEncoder;
    SecretKey secretKey;
    StreamsBuilderFactoryBean streamsBuilderFactoryBean;
    @NonFinal
    KafkaStreams kafkaStreams;
    @NonFinal
    ReadOnlyKeyValueStore<String, AuthDto> authStore;

    @PostConstruct
    public void init() {
        new Thread(() -> {
            try {
                while (!streamsBuilderFactoryBean.isRunning()) {
                    Thread.sleep(1000);
                    log.info("Waiting for streams to start...");
                }
                kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
                authStore = kafkaStreams.store(
                    StoreQueryParameters.fromNameAndType(
                        "user-service.public.auth",
                        QueryableStoreTypes.keyValueStore()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @KafkaHandler
    public String login(LoginRequest loginRequest) {
        final AuthDto authDto = authStore.get(loginRequest.getLogin());

        if (authDto != null
            && passwordEncoder.matches(
            loginRequest.getPassword(),
            authDto.getPassword())
        ) {
            return this.generateToken(loginRequest.getLogin());
        }

        throw new IllegalArgumentException("Incorrect username or password");
    }

    @KafkaHandler
    @Transactional
    protected String register(RegisterRequest registerRequest) {
        if (authStore.get(registerRequest.getEmail()) != null) {
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
                .name("name")
                .login(login)
                .balance(0)
                .status("normal")
                .hasPremium(false)
                .build());

        return login;
    }

    @KafkaHandler
    public String token(String token) {
        final Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        if (Instant.now().isBefore(claims.getExpiration().toInstant())) {
            if (authStore.get(claims.getSubject()) != null) {
                return claims.getSubject();
            }
        }

        throw new IllegalArgumentException("Expired or invalid token");
    }

    protected String generateToken(String login) {
        return Jwts.builder()
            .subject(login)
            .expiration(Date.from(
                Instant.now().plus(
                    Duration.ofMinutes(
                        authProperties.getExpirationMinutes()))))
            .signWith(secretKey)
            .compact();
    }
}
