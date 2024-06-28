package com.slow3586.bettingplatform.userservice.customer;

import com.slow3586.bettingplatform.api.userservice.dto.CustomerDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@KafkaListener(topics = "customer.request", errorHandler = "replyingKafkaTemplateErrorHandler")
public class CustomerService {
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;

    @SendTo("customer.response")
    @KafkaHandler
    public CustomerDto getByUser(String login) {
        return customerRepository.findByLogin(login)
            .map(customerMapper::toDto)
            .orElseThrow();
    }
}
