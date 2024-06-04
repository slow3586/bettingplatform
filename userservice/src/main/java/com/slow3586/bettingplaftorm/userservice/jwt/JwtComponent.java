package com.slow3586.bettingplaftorm.userservice.jwt;

import com.slow3586.bettingplaftorm.userservice.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class JwtComponent {
    JwtProperties jwtProperties;
    UserRepository userRepository;

    public Mono<String> generateToken(UUID id) {
        return Mono.just(
            Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(id.toString())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * jwtProperties.getExpirationMinutes()))
                .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getToken())))
                .compact());
    }

    public Mono<String> getTokenUser(String token) {
        return Mono.just(token)
            .map(t -> Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getToken())))
                .requireIssuer("v1")
                .build()
                .parseSignedClaims(token)
                .getPayload())
            .filter(claims -> claims.getExpiration().before(new Date()))
            .filterWhen(claims -> userRepository.existsById(UUID.fromString(claims.getSubject())))
            .map(Claims::getSubject);
    }
}
