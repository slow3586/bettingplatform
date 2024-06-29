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

    @Transactional
    public UUID process(String token) {
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

        final CustomerEntity customer = customerRepository
            .findByName(paymentDto.getLogin())
            .orElseThrow(() -> new IllegalArgumentException("Unknown customer"));
        customer.setBalance(customer.getBalance() + paymentDto.getValue());
        customerRepository.save(customer);

        return paymentRepository.save(paymentMapper.toEntity(paymentDto)).getId();
    }
}
