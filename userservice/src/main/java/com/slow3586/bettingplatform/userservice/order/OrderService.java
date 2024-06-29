package com.slow3586.bettingplatform.userservice.order;

import com.slow3586.bettingplatform.api.userservice.dto.OrderRequest;
import com.slow3586.bettingplatform.userservice.customer.CustomerEntity;
import com.slow3586.bettingplatform.userservice.customer.CustomerRepository;
import com.slow3586.bettingplatform.userservice.product.ProductEntity;
import com.slow3586.bettingplatform.userservice.product.ProductRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class OrderService {
    CustomerRepository customerRepository;
    OrderRepository orderRepository;
    ProductRepository productRepository;
    OrderMapper orderMapper;

    @Transactional
    @KafkaListener(topics = "order.process")
    public void process(
        @Header(KafkaHeaders.RECEIVED_KEY) String userLogin,
        @Payload OrderRequest orderRequest
    ) {
        final CustomerEntity customerEntity = customerRepository
            .findByName(userLogin)
            .orElseThrow(() -> new IllegalArgumentException("Unknown customer"));

        final ProductEntity productEntity = productRepository
            .findById(orderRequest.getProductId())
            .orElseThrow(() -> new IllegalArgumentException("Unknown product"));

        if (customerEntity.getBalance() <= productEntity.getPrice()) {
            throw new IllegalStateException("Not enough money");
        }

        customerEntity.setBalance(customerEntity.getBalance() - productEntity.getPrice());
        customerRepository.save(customerEntity);

        orderRepository.save(OrderEntity.builder()
            .userLogin(userLogin)
            .productId(orderRequest.getProductId())
            .time(Instant.now())
            .build());
    }

}
