package com.slow3586.bettingplatform.userservice.rest.order;

import com.slow3586.bettingplatform.api.kafka.ErrorHandlingVProcessor;
import com.slow3586.bettingplatform.api.kafka.KafkaStreamsUtils;
import com.slow3586.bettingplatform.api.userservice.dto.CustomerDto;
import com.slow3586.bettingplatform.api.userservice.dto.OrderRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class OrderStreams {
    OrderRepository orderRepository;
    OrderMapper orderMapper;
    @Qualifier("defaultKafkaStreamsBuilder") StreamsBuilder streamsBuilder;

    @Bean
    public StreamsBuilder orderProcess() {
        streamsBuilder.stream("order.request", KafkaStreamsUtils.consumed(OrderRequest.class))
            .leftJoin(streamsBuilder.table("user-service.public.customer", KafkaStreamsUtils.consumed(CustomerDto.class)), KeyValue::pair)
            .processValues(ErrorHandlingVProcessor.factory(
                (record) -> {
                    final OrderRequest orderRequest = record.value().key;
                    final CustomerDto customerDto = record.value().value;

                    if (customerDto == null) {
                        throw new IllegalStateException("Unknown customer");
                    }

                    if (customerDto.getBalance() < 0) {
                        throw new IllegalStateException("Not enough balance");
                    }

                    return orderRepository.save(
                            OrderEntity.builder()
                                .login(customerDto.getLogin())
                                .time(Instant.now())
                                .product(orderRequest.getProduct())
                                .build()
                        ).getId()
                        .toString();
                })).to("order.response", KafkaStreamsUtils.produced(String.class));

        return streamsBuilder;
    }
}
