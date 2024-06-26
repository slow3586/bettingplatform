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

    @KafkaHandler
    public String login(LoginRequest loginRequest) {
        KafkaStreams kafkaStreams = streamsBuilderFactoryBean.getKafkaStreams();
        ReadOnlyKeyValueStore<String, String> loginUserIdStore =
            kafkaStreams.store(StoreQueryParameters.fromNameAndType(
                "user-service.table.auth.login.user_id",
                QueryableStoreTypes.keyValueStore()));
        String userId = loginUserIdStore.get(loginRequest.getLogin());
        if (userId != null) {
            ReadOnlyKeyValueStore<String, String> loginPasswordStore =
                kafkaStreams.store(StoreQueryParameters.fromNameAndType(
                    "user-service.table.auth.login.password",
                    QueryableStoreTypes.keyValueStore()));
            if (passwordEncoder.matches(
                loginRequest.getPassword(),
                loginPasswordStore.get(loginRequest.getLogin()))
            ) {
                return this.generateToken(UUID.fromString(userId));
            }
        }
        throw new IllegalArgumentException("Incorrect username or password");
    }

    @KafkaHandler
    protected UUID register(RegisterRequest registerRequest) {
        if (authRepository.existsByLogin(registerRequest.getEmail())) {
            throw new IllegalArgumentException("Email address already in use");
        }

        final AuthEntity authEntity = authRepository.save(
            AuthEntity.builder()
                .userId(UUID.randomUUID())
                .login(registerRequest.getEmail())
                .email(registerRequest.getEmail())
                .role("user")
                .status("new")
                .password(registerRequest.getPassword())
                .build());

        try {
            customerRepository.save(
                CustomerEntity.builder()
                    .name(authEntity.getLogin())
                    .userId(authEntity.getUserId())
                    .balance(0)
                    .status("normal")
                    .hasPremium(false)
                    .build());
        } catch (Exception e) {
            authRepository.delete(authEntity);
            throw e;
        }

        return authEntity.getUserId();
    }

    @KafkaHandler
    public UUID token(String token) {
        return Mono.just(token)
            .publishOn(Schedulers.boundedElastic())
            .flatMap(this::getTokenSubject)
            .map(UUID::fromString)
            .filter(authRepository::existsByUserId)
            .block(Duration.ofMinutes(1));
    }

    protected String generateToken(UUID id) {
        return Jwts.builder()
            .subject(id.toString())
            .expiration(Date.from(
                Instant.now().plus(
                    Duration.ofMinutes(
                        authProperties.getExpirationMinutes()))))
            .signWith(secretKey)
            .compact();
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
