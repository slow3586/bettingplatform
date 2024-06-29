package com.slow3586.bettingplatform.userservice.customer;

import com.slow3586.bettingplatform.api.userservice.dto.CustomerDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class CustomerService {
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;

    public CustomerDto getByUser(String login) {
        CustomerEntity customerEntity = customerRepository
            .findByLogin(login)
            .orElseThrow(() -> new IllegalArgumentException("Invalid login"));

        return customerMapper.toDto(customerEntity);
    }
}
