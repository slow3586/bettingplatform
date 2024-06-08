package com.slow3586.bettingplaftorm.websocketservice.client;

import com.slow3586.bettingplaftorm.api.BetMakeRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.UUID;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public class BetServiceClient {
    WebClient webClient;
    @NonFinal
    @Value("${app.bet-service-url}")
    String betServiceUrl;

    public UUID make(BetMakeRequest betMakeRequest) {
        return webClient
            .put()
            .uri(betServiceUrl)
            .bodyValue(betMakeRequest)
            .retrieve()
            .bodyToMono(UUID.class)
            .block(Duration.ofSeconds(5));
    }
}
