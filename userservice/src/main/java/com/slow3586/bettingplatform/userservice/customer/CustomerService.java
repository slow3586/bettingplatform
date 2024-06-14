package com.slow3586.bettingplatform.userservice.customer;

import com.slow3586.bettingplatform.api.SecurityUtils;
import com.slow3586.bettingplatform.api.userservice.CustomerDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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
        return this.getPrivateByUser(SecurityUtils.getPrincipalId());
    }

    public Mono<CustomerDto> getPrivateByUser(UUID uuid) {
        return Mono.just(uuid)
            .mapNotNull(customerRepository::findById)
            .flatMap(Mono::justOrEmpty)
            .map(customerMapper::toDto);
    }

    public Mono<CustomerDto> getPublicByUser(UUID uuid) {
        return this.getPrivateByUser(SecurityUtils.getPrincipalId());
    }
}
