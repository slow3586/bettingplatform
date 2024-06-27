package com.slow3586.bettingplatform.userservice.rest.auth;

import com.slow3586.bettingplatform.api.userservice.dto.RegisterRequest;
import com.slow3586.bettingplatform.userservice.rest.customer.CustomerEntity;
import com.slow3586.bettingplatform.userservice.rest.customer.CustomerRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class AuthService {
    AuthRepository authRepository;
    CustomerRepository customerRepository;
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
