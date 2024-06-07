package com.slow3586.bettingplaftorm.betservice.bet;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController(value = "bets")
@RequiredArgsConstructor
public class BetServiceRest {
    BetService betService;

    @PostMapping
    public Mono<Void> make(
        String username,
        Boolean value
    ) {
        return betService.make(username, value);
    }

    protected Mono<UUID> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .map(Authentication::getPrincipal)
            .cast(org.springframework.security.core.userdetails.User.class)
            .map(org.springframework.security.core.userdetails.User::getUsername)
            .map(UUID::fromString)
            .switchIfEmpty(Mono.empty());
    }
}
