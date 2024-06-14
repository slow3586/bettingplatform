package com.slow3586.bettingplatform.userservice.customer;

import com.slow3586.bettingplatform.api.SecurityUtils;
import com.slow3586.bettingplatform.api.userservice.CustomerDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class CustomerService {
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;

    public List<CustomerDto> getByCurrentUser() {
        return this.getPrivateByUser(SecurityUtils.getPrincipalId());
    }

    public List<CustomerDto> getPrivateByUser(UUID uuid) {
        return customerRepository.findById(uuid)
            .stream()
            .map(customerMapper::toDto)
            .toList();
    }

    public List<CustomerDto> getPublicByUser(UUID uuid) {
        return null;
    }
}
