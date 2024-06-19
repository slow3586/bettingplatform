package com.slow3586.bettingplatform.userservice.payment;

import com.slow3586.bettingplatform.api.userservice.PaymentDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class PaymentService {
    PaymentRepository paymentRepository;
    PaymentMapper paymentMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    public Mono<UUID> save(PaymentDto paymentDto) {
        return Mono.just(paymentDto)
            .publishOn(Schedulers.boundedElastic())
            .map(dto -> {
                dto.setId(paymentRepository.save(paymentMapper.toEntity(paymentDto)).getId());
                return dto;
            })
            .map(dto -> {
                kafkaTemplate.send("payment", paymentDto);
                return dto.getId();
            });
    }

    public Mono<Void> processPayment(String token) {
        return Mono.justOrEmpty(token)
            .filter(StringUtils::isNotBlank)
            .mapNotNull(t -> Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode("key")))
                .build()
                .parseSignedClaims(token)
                .getPayload())
            .filter(claims -> Instant.now().isBefore(claims.getExpiration().toInstant()))
            .mapNotNull(claims -> PaymentDto.builder()
                .value(Double.parseDouble(claims.get("value").toString()))
                .userId(UUID.fromString(claims.getSubject()))
                .paidAt(Instant.now())
                .processedAt(Instant.now())
                .source("pay")
                .build())
            .flatMap(this::save)
            .then();
    }
}
