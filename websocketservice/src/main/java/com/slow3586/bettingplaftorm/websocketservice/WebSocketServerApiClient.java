package com.slow3586.bettingplaftorm.websocketservice;

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
public class WebSocketServerApiClient {
    WebClient.Builder webClientBuilder;
    @NonFinal
    @Value("${main-app-url:http://localhost:8080}")
    String mainAppUrl;

    public String getUser(String jwt) {
        return "user";
        /*
        return webClientBuilder
            .baseUrl(mainAppUrl)
            .build()
            .post()
            .uri("/getUser")
            .bodyValue(jwt)
            .retrieve()
            .bodyToMono(String.class)
            .block(Duration.ofSeconds(5));

         */
    }
}
