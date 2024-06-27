package com.slow3586.bettingplatform.userservice.payment;

import com.slow3586.bettingplatform.api.SecurityUtils;
import com.slow3586.bettingplatform.api.kafka.KafkaRestUtils;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequestMapping("payment")
public class PaymentRest {
    ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;
    SecretKey secretKey;

    @GetMapping("debug")
    @PreAuthorize("isAuthenticated()")
    @Operation(security = @SecurityRequirement(name = "BearerAuth"))
    public Mono<String> debug() {
        return SecurityUtils.getReactivePrincipalId()
            .map(user -> Jwts.builder()
                .subject(user)
                .claim("value", 100d)
                .expiration(Date.from(
                    Instant.now().plus(
                        Duration.ofMinutes(1))))
                .signWith(secretKey)
                .compact());
    }

    @PostMapping
    public Mono<Void> processPayment(String token) {
        return this.sendAndReceive(token).then();
    }

    protected Mono<Object> sendAndReceive(Object object) {
        return KafkaRestUtils.sendAndReceive(
            replyingKafkaTemplate,
            "payment.request",
            object);
    }
}
