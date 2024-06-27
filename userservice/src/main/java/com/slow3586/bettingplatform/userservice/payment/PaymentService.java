package com.slow3586.bettingplatform.userservice.payment;

import com.slow3586.bettingplatform.api.userservice.dto.PaymentDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@SendTo("payment.response")
@KafkaListener(topics = "payment.request", errorHandler = "replyingKafkaTemplateErrorHandler")
public class PaymentService {
    PaymentRepository paymentRepository;
    PaymentMapper paymentMapper;
    SecretKey secretKey;

    @KafkaHandler
    public void processPayment(String token) {
        if (StringUtils.isBlank(token)) {
            return;
        }

        final Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        if (Instant.now().isAfter(claims.getExpiration().toInstant())) {
            return;
        }

        final PaymentDto paymentDto = PaymentDto.builder()
            .value(Double.parseDouble(claims.get("value").toString()))
            .login(claims.getSubject())
            .time(claims.getExpiration().toInstant())
            .source("pay")
            .build();

        paymentRepository.save(paymentMapper.toEntity(paymentDto));
    }
}
