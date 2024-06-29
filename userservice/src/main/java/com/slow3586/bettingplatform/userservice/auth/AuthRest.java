package com.slow3586.bettingplatform.userservice.auth;

import com.slow3586.bettingplatform.api.userservice.dto.LoginRequest;
import com.slow3586.bettingplatform.api.userservice.dto.RegisterRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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

    @PostMapping(value = "login")
    public String login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping(value = "register")
    public String register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping(value = "check_token")
    public String checkToken(@RequestBody String token) {
        return authService.checkToken(token);
    }

}
