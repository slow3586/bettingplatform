package com.slow3586.bettingplatform.userservice.auth;

import com.slow3586.bettingplatform.api.SecurityUtils;
import com.slow3586.bettingplatform.api.userservice.LoginRequest;
import com.slow3586.bettingplatform.api.userservice.RegisterRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
public class AuthServiceRest {
    AuthService authService;

    @PostMapping(value = "login")
    public Mono<ResponseEntity<String>> login(@RequestBody LoginRequest request) {
        return authService.login(request)
            .map(jwt ->
                ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE,
                        new HttpCookie(
                            SecurityUtils.AUTH_HEADER_NAME,
                            SecurityUtils.BEARER_PREFIX + jwt
                        ).toString())
                    .build());
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
