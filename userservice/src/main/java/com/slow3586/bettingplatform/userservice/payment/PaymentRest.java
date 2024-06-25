package com.slow3586.bettingplatform.userservice.payment;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequestMapping("payment")
public class PaymentRest {
    PaymentService paymentService;

    @PostMapping("payment")
    public Mono<Void> processPayment(String token) {
        return paymentService.processPayment(token);
    }
}
