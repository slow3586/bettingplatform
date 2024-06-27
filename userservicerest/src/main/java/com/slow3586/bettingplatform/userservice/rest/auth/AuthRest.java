package com.slow3586.bettingplatform.userservice.rest.auth;

import com.slow3586.bettingplatform.api.kafka.KafkaRestUtils;
import com.slow3586.bettingplatform.api.userservice.dto.LoginRequest;
import com.slow3586.bettingplatform.api.userservice.dto.RegisterRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
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

    @PostMapping(value = "login")
    public Mono<Object> login(@RequestBody LoginRequest request) {
        return KafkaRestUtils.sendAndReceive(
            replyingKafkaTemplate,
            "auth.request.login",
            request.getLogin(),
            request);
    }

    @PostMapping(value = "register")
    public Mono<Object> register(@RequestBody RegisterRequest request) {
        return KafkaRestUtils.sendAndReceive(
            replyingKafkaTemplate,
            "auth.request",
            request.getEmail(),
            request);
    }

    @PostMapping(value = "token")
    public Mono<Object> token(@RequestBody String request) {
        return KafkaRestUtils.sendAndReceive(
            replyingKafkaTemplate,
            "auth.request.token",
            request);
    }

}
