package com.slow3586.bettingplaftorm.websocketservice.client;

import com.slow3586.bettingplaftorm.api.BetMakeRequest;
import com.slow3586.bettingplaftorm.api.PriceEntityDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public class BetServiceClient {
    WebClient.Builder webClientBuilder;
    @NonFinal
    @Value("${app.bet-service-url}")
    String betServiceUrl;

    public UUID make(BetMakeRequest betMakeRequest) {
        return webClientBuilder
            .baseUrl(betServiceUrl)
            .build()
            .put()
            .uri("/bet/make")
            .bodyValue(betMakeRequest)
            .retrieve()
            .bodyToMono(UUID.class)
            .block(Duration.ofSeconds(5));
    }

    public List<String> priceLatest() {
        return webClientBuilder
            .baseUrl(betServiceUrl)
            .build()
            .get()
            .uri("/price/latest")
            .retrieve()
            .bodyToFlux(String.class)
            .collectList()
            .block(Duration.ofSeconds(5));
    }
}
