package com.slow3586.bettingplaftorm.betservice.price;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class PriceService {
    PriceServiceProperties priceServiceProperties;
    WebClient webClient;
    KafkaTemplate<String, Double> kafkaProducer;

    @Async
    @Scheduled(cron = "*/5 * * * * *")
    public void updatePrice() {
        final Pattern pricePattern = Pattern.compile(priceServiceProperties.getResponseRegex());
        webClient
            .get()
            .uri(priceServiceProperties.getRequestPath())
            .retrieve()
            .bodyToMono(String.class)
            .map(pricePattern::matcher)
            .map(m -> m.matches() ? m.group() : null)
            .switchIfEmpty(Mono.error(new RuntimeException("Not found")))
            .map(Double::valueOf)
            .map(price ->
                kafkaProducer.send(
                    "price.updated",
                    priceServiceProperties.getRequestInstrument(),
                    price))
            .flatMap(Mono::fromFuture)
            .subscribe();
    }
}
