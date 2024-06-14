package com.slow3586.bettingplatform.userservice;

import com.slow3586.bettingplatform.api.SecurityUtils;
import com.slow3586.bettingplatform.userservice.auth.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.annotation.NonNull;

import java.time.Duration;

@Service
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserServiceSecurityWebFilter implements WebFilter {
    @Lazy
    AuthService authService;

    @Override
    @NonNull
    public Mono<Void> filter(
        final ServerWebExchange exchange,
        @NonNull final WebFilterChain chain
    ) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().get(SecurityUtils.AUTH_HEADER_NAME))
            .filter(l -> !l.contains(null))
            .flatMapMany(Flux::fromIterable)
            .single()
            .filter(s -> s.startsWith(SecurityUtils.BEARER_PREFIX))
            .mapNotNull(s -> s.substring(SecurityUtils.BEARER_PREFIX.length()))
            .publishOn(Schedulers.boundedElastic())
            .map(authService::token)
            .map(userId ->
                new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    AuthorityUtils.createAuthorityList("auth")))
            .timeout(Duration.ofSeconds(5))
            .onErrorReturn(new UsernamePasswordAuthenticationToken(null, null))
            .map(SecurityContextImpl::new)
            .transform(securityContextMono -> chain
                .filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder
                    .withSecurityContext(securityContextMono)));
    }
}
