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

    @GetMapping("/private/{uuid}")
    @PreAuthorize("isAuthenticated() && #uuid.toString() == authentication.name")
    @Operation(security = @SecurityRequirement(name = "BearerAuth"))
    public Mono<CustomerDto> getPrivateByUser(@PathVariable("uuid") UUID uuid) {
        return customerService.getPrivateByUser(uuid);
    }

    @GetMapping("/public/{uuid}")
    public Mono<CustomerDto> getPublicByUser(@PathVariable("uuid") UUID uuid) {
        return customerService.getPublicByUser(uuid);
    }

    @Operation(security = @SecurityRequirement(name = "BearerAuth"))
    public void buyPremium(){

    }


}
