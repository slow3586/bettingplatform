package com.slow3586.bettingplatform.userservice.order;

import com.slow3586.bettingplatform.api.userservice.dto.OrderRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;

    public UUID make(
        @Header(KafkaHeaders.RECEIVED_KEY) String login,
        OrderRequest orderRequest
    ) {
        OrderEntity orderEntity = OrderEntity.builder()
            .login(login)
            .time(Instant.now())
            .product(orderRequest.getProduct())
            .build();

        return orderRepository.save(orderEntity).getId();
    }
}
