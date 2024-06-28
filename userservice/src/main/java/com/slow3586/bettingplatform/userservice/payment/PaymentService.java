package com.slow3586.bettingplatform.userservice.payment;

import com.slow3586.bettingplatform.api.userservice.dto.PaymentDto;
import com.slow3586.bettingplatform.userservice.customer.CustomerEntity;
import com.slow3586.bettingplatform.userservice.customer.CustomerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class PaymentService {
    CustomerRepository customerRepository;
    PaymentRepository paymentRepository;
    PaymentMapper paymentMapper;
    SecretKey secretKey;

    @KafkaListener(topics = "payment.request", errorHandler = "replyingKafkaTemplateErrorHandler")
    @SendTo("payment.response")
    @Transactional
    public UUID processPayment(String token) {
        if (StringUtils.isBlank(token)) {
            throw new IllegalArgumentException("Invalid token");
        }

        final Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();

        if (Instant.now().isAfter(claims.getExpiration().toInstant())) {
            throw new IllegalArgumentException("Invalid token");
        }

        final PaymentDto paymentDto = PaymentDto.builder()
            .value(Double.parseDouble(claims.get("value").toString()))
            .login(claims.getSubject())
            .time(claims.getExpiration().toInstant())
            .source("pay")
            .build();

        CustomerEntity customer = customerRepository.findByLogin(paymentDto.getLogin()).orElseThrow();
        customer.setBalance(customer.getBalance() + paymentDto.getValue());
        customerRepository.save(customer);

        return paymentRepository.save(paymentMapper.toEntity(paymentDto)).getId();
    }
}
