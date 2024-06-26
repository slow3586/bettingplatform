package com.slow3586.bettingplatform.userservice;

import com.slow3586.bettingplatform.api.SecurityUtils;
import com.slow3586.bettingplatform.userservice.auth.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;

@Component
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceSecurityWebFilter implements WebFilter {
    AuthService authService;

    @Override
    @NonNull
    public Mono<Void> filter(
        final ServerWebExchange exchange,
        @NonNull final WebFilterChain chain
    ) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().get(SecurityUtils.AUTH_HEADER_NAME))
            .map(l -> l.get(0))
            .filter(s -> s.startsWith(SecurityUtils.BEARER_PREFIX))
            .mapNotNull(s -> s.substring(SecurityUtils.BEARER_PREFIX.length()))
            .map(authService::token)
            .map(userId ->
                new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    AuthorityUtils.createAuthorityList("user")))
            .single()
            .onErrorReturn(new UsernamePasswordAuthenticationToken(null, null))
            .flatMap(token -> chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder
                    .withAuthentication(token)));
    }
}
