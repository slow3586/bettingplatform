package com.slow3586.bettingplatform.userservice.order;

import com.slow3586.bettingplatform.api.userservice.dto.AuthDto;
import com.slow3586.bettingplatform.api.userservice.dto.CustomerDto;
import com.slow3586.bettingplatform.api.userservice.dto.OrderRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.processor.To;
import org.apache.kafka.streams.processor.api.FixedKeyProcessor;
import org.apache.kafka.streams.processor.api.FixedKeyProcessorSupplier;
import org.apache.kafka.streams.processor.api.FixedKeyRecord;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.internals.RocksDBKeyValueBytesStoreSupplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@Slf4j
public class OrderConfig {
    OrderRepository orderRepository;

    @Bean
    public Topology orderProcess(StreamsBuilder streamsBuilder) {
        final JsonSerde<AuthDto> valueSerde = new JsonSerde<>(AuthDto.class);
        valueSerde.deserializer().addTrustedPackages("*");
        valueSerde.deserializer().ignoreTypeHeaders();

        Materialized<String, AuthDto, KeyValueStore<Bytes, byte[]>> loginUserId =
            Materialized.as(
                new RocksDBKeyValueBytesStoreSupplier(
                    "user-service.public.auth",
                    true));
        streamsBuilder.globalTable("user-service.public.auth",
            loginUserId
                .withKeySerde(Serdes.String())
                .withValueSerde(valueSerde));

        final JsonSerde<CustomerDto> customerDtoJsonSerde = new JsonSerde<>(CustomerDto.class);
        customerDtoJsonSerde.deserializer().addTrustedPackages("*");

        final JsonSerde<OrderRequest> orderRequestJsonSerde = new JsonSerde<>(OrderRequest.class);
        orderRequestJsonSerde.deserializer().addTrustedPackages("*");

        final KTable<String, CustomerDto> authDtoKTable = streamsBuilder.table("user-service.public.customer",
            Materialized.with(Serdes.String(), customerDtoJsonSerde));

        final Map<String, KStream<String, KeyValue<OrderRequest, CustomerDto>>> streamMap =
            streamsBuilder.stream("order.request",
                    Consumed.with(Serdes.String(), orderRequestJsonSerde))
                .join(authDtoKTable, KeyValue::pair)
                .split(Named.as("order.request.make."))
                .branch(((key, value) -> value.value.getBalance() > 0), Branched.as("valid"))
                .defaultBranch(Branched.as("invalid"));

        final KStream<String, Object> streamInvalid = streamMap.get("order.request.make.valid")
            .mapValues((key, value) -> value.key)
            .mapValues((key, value) -> OrderEntity.builder()
                .login(key)
                .time(Instant.now())
                .product(value.getProduct())
                .build())
            .mapValues((key, value) -> orderRepository.save(value).getId().toString());

        final KStream<String, Object> streamValid = streamMap.get("order.request.make.invalid")
            .processValues(() -> new FixedKeyProcessor<String, KeyValue<OrderRequest, CustomerDto>, Object>() {
                @Override
                public void process(FixedKeyRecord<String, KeyValue<OrderRequest, CustomerDto>> record) {
                    record.withValue(null)
                        .headers()
                        .add(KafkaHeaders.EXCEPTION_FQCN, "IllegalStateException".getBytes(StandardCharsets.UTF_8))
                        .add(KafkaHeaders.EXCEPTION_MESSAGE, "No money!".getBytes(StandardCharsets.UTF_8));
                }
            });

        streamValid.merge(streamInvalid)
            .to("order.response",
                Produced.keySerde(Serdes.String())
                    .withValueSerde(new JsonSerde<>()));

        Topology topology = streamsBuilder.build();

        log.info("#topology {}", topology.describe());

        return topology;
    }
}
