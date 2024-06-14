package com.slow3586.bettingplatform.userservice.auth;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "app.auth")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthProperties {
    String token;
    long expirationMinutes;
}
