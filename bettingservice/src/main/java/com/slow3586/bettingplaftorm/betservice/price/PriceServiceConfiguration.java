package com.slow3586.bettingplaftorm.betservice.price;

import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.slow3586.bettingplaftorm.betservice.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.DoubleSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class PriceServiceConfiguration {
    @Bean
    public WebClient defaultWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder.build();
    }

    @Bean
    public DefaultKafkaProducerFactory<String, Double> kafkaProducerFactoryDouble() {
        return new DefaultKafkaProducerFactory<>(
            Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9091",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DoubleSerializer.class
            ));
    }

    @Bean
    public DefaultKafkaProducerFactory<String, ValueChoice> kafkaProducerFactoryValueChoice() {
        return new DefaultKafkaProducerFactory<>(
            Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9091",
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, new JsonSerializer<ValueChoice>()
            ));
    }

    @Bean
    public KafkaTemplate<String, Double> kafkaTemplateDouble() {
        return new KafkaTemplate<>(kafkaProducerFactoryDouble());
    }

    @Bean
    public KafkaTemplate<String, Boolean> kafkaTemplateValueChoice() {
        return new KafkaTemplate<>(kafkaProducerFactoryValueChoice());
    }
}
