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

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class PriceService {
    PriceServiceProperties priceServiceProperties;
    WebClient webClient;
    KafkaTemplate<String, String> kafkaProducer;
    PriceRepository priceRepository;

    @Async
    @Scheduled(cron = "0/1 * * * * *")
    public void updatePrice() {
        final Pattern pricePattern = Pattern.compile(priceServiceProperties.getResponseRegex());
        webClient
            .get()
            .uri(priceServiceProperties.getRequestPath())
            .retrieve()
            .bodyToMono(String.class)
            .flatMap(priceString -> {
                final Matcher matcher = pricePattern.matcher(priceString);
                return matcher.matches()
                    ? Mono.just(matcher.group(1))
                    : Mono.error(new IllegalArgumentException("Price not found: " + priceString));
            })
            //.mapNotNull(Double::valueOf)
            .mapNotNull(price -> {
                priceRepository.save(PriceEntity.builder()
                    .instrument(priceServiceProperties.getRequestInstrument())
                    .time(LocalDateTime.now(ZoneId.of("UTC")))
                    .value(Double.parseDouble(price))
                    .build());
                return kafkaProducer.send(
                    "price.update",
                    priceServiceProperties.getRequestInstrument(),
                    price);
            })
            .flatMap(Mono::fromFuture)
            .subscribe();
    }

    public List<String> getLatest() {
        return priceRepository.findLatest();
    }
}
