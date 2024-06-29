package com.slow3586.bettingplatform.userservice.auth;

import com.slow3586.bettingplatform.api.userservice.dto.AuthLoginRequest;
import com.slow3586.bettingplatform.api.userservice.dto.AuthRegisterRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class AuthRest {
    AuthService authService;
    KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping(value = "login")
    public String login(@RequestBody AuthLoginRequest request) {
        return authService.login(request);
    }

    @PostMapping(value = "register")
    public void register(@RequestBody AuthRegisterRequest request) {
        kafkaTemplate.send("auth.register.request", request.getEmail(), request);
    }

    @PostMapping(value = "check_token")
    public String checkToken(@RequestBody String token) {
        return authService.checkToken(token);
    }

}
