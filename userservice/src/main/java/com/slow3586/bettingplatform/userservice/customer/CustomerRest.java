package com.slow3586.bettingplatform.userservice.customer;

import com.slow3586.bettingplatform.api.SecurityUtils;
import com.slow3586.bettingplatform.api.kafka.KafkaRestUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequestMapping("customer")
public class CustomerRest {
    CustomerService customerService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(security = @SecurityRequirement(name = "BearerAuth"))
    public Mono<Object> getByCurrentUser() {
        return SecurityUtils.getReactivePrincipalId()
            .flatMap(this::getByLogin);
    }

    @GetMapping("/{login}")
    public Mono<Object> getByLogin(@PathVariable("login") String login) {
        return customerService.getByUser(login);
    }
}
