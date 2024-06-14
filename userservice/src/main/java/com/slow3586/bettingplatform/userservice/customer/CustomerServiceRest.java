package com.slow3586.bettingplatform.userservice.customer;

import com.slow3586.bettingplatform.api.userservice.CustomerDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class CustomerServiceRest {
    CustomerService customerService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public List<CustomerDto> getCurrent() {
        return customerService.getByCurrentUser();
    }

    @GetMapping("/private/{uuid}")
    @PreAuthorize("isAuthenticated()")
    public List<CustomerDto> getPrivateByUser(@PathVariable("uuid") UUID uuid) {
        return customerService.getPrivateByUser(uuid);
    }

    @GetMapping("/public/{uuid}")
    @PreAuthorize("isAuthenticated()")
    public List<CustomerDto> getPublicByUser(@PathVariable("uuid") UUID uuid) {
        return customerService.getPublicByUser(uuid);
    }
}
