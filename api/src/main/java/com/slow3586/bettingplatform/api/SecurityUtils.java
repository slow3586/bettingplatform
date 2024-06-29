package com.slow3586.bettingplatform.api;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import reactor.core.publisher.Mono;

public class SecurityUtils {
    public static String BEARER_PREFIX = "Bearer ";
    public static String AUTH_HEADER_NAME = "Authorization";

    public static String getPrincipalId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AccessDeniedException("Not authenticated");
        }

        return authentication.getPrincipal().toString();
    }

    public static Mono<String> getReactivePrincipalId() {
        return ReactiveSecurityContextHolder.getContext()
            .mapNotNull(SecurityContext::getAuthentication)
            .mapNotNull(Authentication::getPrincipal)
            .map(Object::toString);
    }
}
