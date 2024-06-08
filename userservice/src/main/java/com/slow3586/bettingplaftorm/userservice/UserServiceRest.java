package com.slow3586.bettingplaftorm.userservice;

import com.slow3586.bettingplaftorm.userservice.dto.LoginRequest;
import com.slow3586.bettingplaftorm.userservice.dto.RegisterRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class UserServiceRest {
    UserService userService;

    @PostMapping(value = "login")
    public Mono<String> login(@RequestBody LoginRequest request) {
        return userService.login(request);
    }

    @PostMapping(value = "register")
    public Mono<String> register(@RequestBody RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping(value = "token")
    public Mono<String> token(@RequestBody String token) {
        return userService.token(token);
    }
}
