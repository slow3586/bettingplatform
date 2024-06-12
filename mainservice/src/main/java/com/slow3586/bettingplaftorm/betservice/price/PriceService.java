package com.slow3586.bettingplaftorm.betservice.price;

import com.slow3586.bettingplaftorm.api.PriceDto;
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
    static String TOPIC_GET = "price.get";
    PriceProperties priceProperties;
    WebClient webClient;
    KafkaTemplate<String, PriceDto> priceKafkaProducer;
    PriceRepository priceRepository;
    PriceMapper priceMapper;
    Pattern pricePattern;

    public List<PriceDto> getLatest() {
        return priceRepository.findLatest()
            .stream()
            .map(priceMapper::toDto)
            .toList();
    }

    public void save(final PriceDto priceDto) {
        final PriceEntity price = priceRepository.save(priceMapper.toEntity(priceDto));
        priceKafkaProducer.send(TOPIC_GET, priceMapper.toDto(price));
    }

    @Async
    @Scheduled(cron = "0/1 * * * * *")
    public void updatePrice() {
        webClient.get()
            .retrieve()
            .bodyToMono(String.class)
            .flatMap(priceString -> {
                final Matcher matcher = pricePattern.matcher(priceString);
                return matcher.find()
                    ? Mono.just(matcher.group())
                    : Mono.error(new IllegalArgumentException("Price not found: " + priceString));
            })
            .mapNotNull(d -> PriceDto.builder()
                .value(Double.parseDouble(d))
                .instrument(priceProperties.getRequestInstrument())
                .time(LocalDateTime.now(ZoneId.of("UTC")))
                .build())
            .doOnNext(this::save)
            .subscribe();
    }
}
