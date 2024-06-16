package com.slow3586.bettingplatform.betservice.price;

import com.slow3586.bettingplatform.api.mainservice.PriceDto;
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

import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class PriceService {
    PriceProperties priceProperties;
    WebClient webClient;
    KafkaTemplate<String, Object> priceKafkaProducer;
    PriceRepository priceRepository;
    PriceMapper priceMapper;
    Pattern pricePattern;

    public List<PriceDto> getLatest(String instrument) {
        return priceRepository.findLatest(instrument)
            .stream()
            .map(priceMapper::toDto)
            .toList();
    }

    public void save(final PriceDto priceDto) {
        final PriceEntity price = priceRepository.save(priceMapper.toEntity(priceDto));
        priceKafkaProducer.send("price", priceMapper.toDto(price));
    }

    @Async
    @Scheduled(cron = "*/1 * * * * *")
    public void updatePrice() {
        Mono<String> stringMono = webClient
            .get()
            .retrieve()
            .bodyToMono(String.class)
            .flatMap(priceString -> {
                final Matcher matcher = pricePattern.matcher(priceString);
                return matcher.matches()
                    ? Mono.just(matcher.group(1))
                    : Mono.error(new IllegalArgumentException("Price not found: " + priceString));
            });
        Mono.just("100")
            .mapNotNull(d -> PriceDto.builder()
                .value(Double.parseDouble(d))
                .instrument(priceProperties.getRequestInstrument())
                .time(Instant.now())
                .build())
            .doOnNext(this::save)
            .onErrorMap(Exception.class,
                e -> new RuntimeException("PriceService#updatePrice " + e.getMessage(), e))
            .subscribe();
    }
}
