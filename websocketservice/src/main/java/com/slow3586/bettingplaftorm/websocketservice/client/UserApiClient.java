package com.slow3586.bettingplaftorm.websocketservice.client;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public class UserApiClient {
    WebClient webClient;
    @NonFinal
    @Value("${app.user-service-url}")
    String userServiceUrl;

    public String checkToken(String jwt) {
        return "08bdf387-83f1-4f68-906f-38bb7abed369";/*
        return webClient
            .post()
            .uri(userServiceUrl + "/token")
            .bodyValue(jwt)
            .retrieve()
            .bodyToMono(String.class)
            .block(Duration.ofSeconds(5));
            */
    }
}
