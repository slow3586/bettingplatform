package com.slow3586.bettingplatform.userservice.customer;

import com.slow3586.bettingplatform.api.userservice.dto.CustomerDto;
import com.slow3586.bettingplatform.api.userservice.dto.PaymentDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class CustomerService {
    CustomerRepository customerRepository;
    CustomerMapper customerMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    @SendTo("customer.response")
    @KafkaListener(topics = "customer.request", errorHandler = "replyingKafkaTemplateErrorHandler")
    public CustomerDto getByUser(String login) {
        return customerRepository.findByLogin(login)
            .map(customerMapper::toDto)
            .orElseThrow();
    }

    @KafkaListener(topics = "user-service.public.payment", errorHandler = "replyingKafkaTemplateErrorHandler")
    public void paymentEvent(PaymentDto paymentDto) {
        final CustomerEntity customer = customerRepository
            .findByLogin(paymentDto.getLogin())
            .orElseThrow();

        customer.setBalance(customer.getBalance() + paymentDto.getValue());

        customerRepository.save(customer);
    }
}
