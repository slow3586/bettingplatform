package com.slow3586.bettingplatform.userservice.customer;

import com.slow3586.bettingplatform.api.userservice.dto.CustomerDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequestMapping("customer")
public class CustomerRest {
    CustomerService customerService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(security = @SecurityRequirement(name = "BearerAuth"))
    public Mono<CustomerDto> getCurrent() {
        return customerService.getByCurrentUser();
    }

    @GetMapping("/private/{login}")
    @PreAuthorize("isAuthenticated() && #login.toString() == authentication.name")
    @Operation(security = @SecurityRequirement(name = "BearerAuth"))
    public Mono<CustomerDto> getPrivateByUser(@PathVariable("login") String login) {
        return customerService.getPrivateByUser(login);
    }

    @GetMapping("/public/{login}")
    public Mono<CustomerDto> getPublicByUser(@PathVariable("login") String login) {
        return customerService.getPublicByUser(login);
    }

    @Operation(security = @SecurityRequirement(name = "BearerAuth"))
    public void buyPremium(){

    }


}
