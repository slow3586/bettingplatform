package com.slow3586.bettingplatform.userservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class UserServiceConfig {
    @NonFinal
    @Value("${KAFKA_BROKERS:localhost:9092}")
    String kafkaBrokers;
    @NonFinal
    @Value("${KAFKA_SCHEMA_REGISTRY_URL:http://localhost:8095}")
    String kafkaSchemaRegistry;

    @Bean
    public ProducerFactory<String, Object> kafkaProducerFactory() {
        return new DefaultKafkaProducerFactory<>(
            Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers,
                "schema.registry.url", kafkaSchemaRegistry
            ), new StringSerializer(),
            new JsonSerializer<>());
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "user-service-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "2000");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, JsonSerde.class.getName());
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(6);
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(
            Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers,
                ConsumerConfig.GROUP_ID_CONFIG, "user-service",
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
                ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class,
                ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class,
                JsonDeserializer.TRUSTED_PACKAGES, "*"
            ),
            new ErrorHandlingDeserializer<>(),
            new ErrorHandlingDeserializer<>()));

        KafkaTemplate<String, Object> replyTemplate = kafkaTemplate();
        factory.setReplyTemplate(replyTemplate);
        return factory;
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(kafkaProducerFactory());
    }
}
