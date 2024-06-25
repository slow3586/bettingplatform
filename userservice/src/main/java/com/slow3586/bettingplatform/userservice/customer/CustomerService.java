package com.slow3586.bettingplatform.userservice.customer;

import com.slow3586.bettingplatform.api.userservice.dto.AuthDto;
import com.slow3586.bettingplatform.api.userservice.dto.CustomerDto;
import com.slow3586.bettingplatform.api.userservice.dto.RegisterRequest;
import com.slow3586.bettingplatform.userservice.auth.AuthEntity;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class CustomerService {
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    public Mono<CustomerDto> getByCurrentUser() {
        return this.getCurrentUserId().flatMap(this::getPrivateByUser);
    }

    public Mono<CustomerDto> getPrivateByUser(UUID uuid) {
        return Mono.just(uuid)
            .mapNotNull(customerRepository::findByUserId)
            .flatMap(Mono::justOrEmpty)
            .map(customerMapper::toDto)
            .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }

    public Mono<CustomerDto> getPublicByUser(UUID uuid) {
        return getPrivateByUser(uuid);
    }

    public Mono<UUID> getCurrentUserId() {
        return ReactiveSecurityContextHolder.getContext()
            .mapNotNull(SecurityContext::getAuthentication)
            .mapNotNull(Authentication::getPrincipal)
            .map(Object::toString)
            .map(UUID::fromString);
    }

    @KafkaListener(topics = "auth.register")
    protected void register(AuthDto authDto) {
        Mono.just(authDto)
            .publishOn(Schedulers.boundedElastic())
            .doOnNext((request) ->
                customerRepository.save(
                    CustomerEntity.builder()
                        .userId(UUID.randomUUID())
                        .build()))
            .subscribe();
    }
}
