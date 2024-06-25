package com.slow3586.bettingplatform.userservice.auth;

import com.slow3586.bettingplatform.api.userservice.dto.LoginRequest;
import com.slow3586.bettingplatform.api.userservice.dto.RegisterRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class AuthRest {
    AuthService authService;
    ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;

    @PostMapping(value = "login")
    public CompletableFuture<Object> login(@RequestBody LoginRequest request) {
        return replyingKafkaTemplate.sendAndReceive(
                new ProducerRecord<>(
                    "auth.login",
                    request))
            .thenApply(ConsumerRecord::value)
            .toCompletableFuture();
    }

    @PostMapping(value = "register")
    public CompletableFuture<Object> register(@RequestBody RegisterRequest request) {
        return replyingKafkaTemplate.sendAndReceive(
                new ProducerRecord<>(
                    "auth.register",
                    request))
            .thenApply(ConsumerRecord::value)
            .toCompletableFuture();
    }

    @PostMapping(value = "token")
    public Mono<UUID> token(@RequestBody String token) {
        return authService.token(token);
    }
}
