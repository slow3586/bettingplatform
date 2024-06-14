package com.slow3586.bettingplatform.betservice.price;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.regex.Pattern;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class PriceConfig {
    PriceProperties priceProperties;

    @Bean
    public Pattern pricePattern() {
        return Pattern.compile(priceProperties.getResponseRegex());
    }

    @Bean
    public WebClient priceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
            .baseUrl(priceProperties.getRequestPath())
            .build();
    }
}
