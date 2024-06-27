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
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.api.ContextualFixedKeyProcessor;
import org.apache.kafka.streams.processor.api.FixedKeyRecord;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

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

    @Transactional
    @SendTo("auth.response")
    @KafkaListener(topics = "auth.request", errorHandler = "replyingKafkaTemplateErrorHandler")
    protected String register(RegisterRequest registerRequest) {
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
                .name("name")
                .login(login)
                .balance(0)
                .status("normal")
                .hasPremium(false)
                .build());

        return login;
    }
}
