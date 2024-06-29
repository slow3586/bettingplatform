package com.slow3586.bettingplatform.userservice;

import com.slow3586.bettingplatform.api.SecurityUtils;
import com.slow3586.bettingplatform.userservice.auth.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceSecurityWebFilter extends OncePerRequestFilter {
    AuthService authService;

    @Override
    protected void doFilterInternal(
        @NonNull final HttpServletRequest request,
        @NonNull final HttpServletResponse response,
        @NonNull final FilterChain filterChain
    ) throws ServletException, IOException {
        UsernamePasswordAuthenticationToken authentication;
        try {
            final String header = request.getHeader("Authorization");

            if (!header.startsWith(SecurityUtils.BEARER_PREFIX)) {
                throw new IllegalArgumentException("Not an authorization header");
            }

            final String token = header.substring(SecurityUtils.BEARER_PREFIX.length());

            final String userLogin = authService.checkToken(token);

            authentication = new UsernamePasswordAuthenticationToken(
                userLogin,
                null,
                AuthorityUtils.createAuthorityList("user"));
        } catch (Exception e) {
            authentication = new UsernamePasswordAuthenticationToken(null, null);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
}
