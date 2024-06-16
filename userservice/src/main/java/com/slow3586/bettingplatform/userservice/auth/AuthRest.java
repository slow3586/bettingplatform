package com.slow3586.bettingplatform.userservice.auth;

import com.slow3586.bettingplatform.api.userservice.LoginRequest;
import com.slow3586.bettingplatform.api.userservice.RegisterRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class AuthRest {
    AuthService authService;

    @PostMapping(value = "login")
    public Mono<String> login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping(value = "register")
    public Mono<UUID> register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping(value = "token")
    public Mono<UUID> token(@RequestBody String token) {
        return authService.token(token);
    }
}
