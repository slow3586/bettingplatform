package com.slow3586.bettingplatform.userservice.customer;

import com.slow3586.bettingplatform.api.userservice.CustomerDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class CustomerService {
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;

    public Mono<CustomerDto> getByCurrentUser() {
        return this.getCurrentUserId().flatMap(this::getPrivateByUser);
    }

    public Mono<CustomerDto> getPrivateByUser(UUID uuid) {
        return Mono.just(uuid)
            .mapNotNull(customerRepository::findById)
            .flatMap(Mono::justOrEmpty)
            .map(customerMapper::toDto);
    }

    public Mono<CustomerDto> getPublicByUser(UUID uuid) {
        return null;
    }

    public Mono<UUID> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext()
            .mapNotNull(SecurityContext::getAuthentication)
            .mapNotNull(Authentication::getPrincipal)
            .map(Object::toString)
            .map(UUID::fromString);
    }
}
