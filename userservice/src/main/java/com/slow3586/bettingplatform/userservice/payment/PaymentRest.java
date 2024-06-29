package com.slow3586.bettingplatform.userservice.payment;

import com.slow3586.bettingplatform.api.SecurityUtils;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequestMapping("payment")
public class PaymentRest {
    SecretKey secretKey;
    PaymentService paymentService;

    @GetMapping("debug")
    @PreAuthorize("isAuthenticated()")
    @Operation(security = @SecurityRequirement(name = "BearerAuth"))
    public String debug() {
        String userLogin = SecurityUtils.getPrincipalId();
        return Jwts.builder()
            .subject(userLogin)
            .claim("value", 100d)
            .expiration(Date.from(
                Instant.now().plus(
                    Duration.ofMinutes(1))))
            .signWith(secretKey)
            .compact();
    }

    @PostMapping
    public UUID process(@RequestBody String token) {
        return paymentService.process(token);
    }
}
