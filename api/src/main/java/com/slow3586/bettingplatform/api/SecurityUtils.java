package com.slow3586.bettingplatform.api;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

public class SecurityUtils {
    public static String BEARER_PREFIX = "Bearer ";
    public static String AUTH_HEADER_NAME = "Authorization";

    public static UUID getPrincipalId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .map(Authentication::getPrincipal)
            .filter(p -> p instanceof UUID)
            .map(p -> ((UUID) p))
            .orElseThrow(() -> new AccessDeniedException("No authentication"));
    }
}
