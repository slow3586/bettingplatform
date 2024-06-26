package com.slow3586.bettingplatform.userservice.auth;

import com.slow3586.bettingplatform.api.kafka.KafkaRestUtils;
import com.slow3586.bettingplatform.api.userservice.dto.LoginRequest;
import com.slow3586.bettingplatform.api.userservice.dto.RegisterRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class AuthRest {
    ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;
    PasswordEncoder passwordEncoder;

    @SneakyThrows
    @PostMapping(value = "login")
    public Object login(@RequestBody LoginRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return replyingKafkaTemplate.sendAndReceive(
            new ProducerRecord<>(
                "auth.request",
                request))
            .thenApply(ConsumerRecord::value)
            .get();
    }

    @PostMapping(value = "register")
    public Mono<Object> register(@RequestBody RegisterRequest request) {
        return this.sendAndReceive(request);
    }

    @PostMapping(value = "token")
    public Mono<Object> token(@RequestBody String request) {
        return this.sendAndReceive(request);
    }

    protected Mono<Object> sendAndReceive(Object object) {
        return KafkaRestUtils.sendAndReceive(
            replyingKafkaTemplate,
            "auth.request",
            object);
    }
}
