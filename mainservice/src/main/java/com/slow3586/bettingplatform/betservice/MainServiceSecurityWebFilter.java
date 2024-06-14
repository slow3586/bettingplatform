package com.slow3586.bettingplatform.betservice;

import com.slow3586.bettingplatform.api.SecurityUtils;
import com.slow3586.bettingplatform.api.userservice.AuthServiceClient;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

@Service
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class MainServiceSecurityWebFilter extends OncePerRequestFilter {
    AuthServiceClient authServiceClient;

    @Override
    protected void doFilterInternal(
        final HttpServletRequest request,
        @NonNull final HttpServletResponse response,
        final FilterChain filterChain
    ) throws ServletException, IOException {
        Mono.justOrEmpty(request.getHeader("Authorization"))
            .filter(s -> s.startsWith(SecurityUtils.BEARER_PREFIX))
            .mapNotNull(s -> s.substring(SecurityUtils.BEARER_PREFIX.length()))
            .mapNotNull(authServiceClient::token)
            .map(uuid -> new UsernamePasswordAuthenticationToken(
                uuid,
                null,
                AuthorityUtils.createAuthorityList("user")))
            .single()
            .onErrorReturn(new UsernamePasswordAuthenticationToken(
                null,
                null))
            .blockOptional(Duration.ofSeconds(5))
            .ifPresent(token -> SecurityContextHolder.getContext().setAuthentication(token));
        filterChain.doFilter(request, response);
    }
}
