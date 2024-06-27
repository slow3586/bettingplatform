package com.slow3586.bettingplatform.userservice.order;

import com.slow3586.bettingplatform.api.kafka.ErrorHandlingVProcessor;
import com.slow3586.bettingplatform.api.kafka.KafkaStreamsUtils;
import com.slow3586.bettingplatform.api.userservice.dto.CustomerDto;
import com.slow3586.bettingplatform.api.userservice.dto.OrderRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class OrderService {
    OrderRepository orderRepository;
    OrderMapper orderMapper;

    @Autowired
    public StreamsBuilder orderProcess(@Qualifier("defaultKafkaStreamsBuilder") StreamsBuilder streamsBuilder) {
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
