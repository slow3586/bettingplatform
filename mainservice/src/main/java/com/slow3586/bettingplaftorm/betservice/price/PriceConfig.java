package com.slow3586.bettingplaftorm.betservice.price;

import com.slow3586.bettingplaftorm.api.PriceDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.regex.Pattern;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class PriceConfig {
    PriceProperties priceProperties;

    @Bean
    public Pattern pricePattern() {
        return Pattern.compile(priceProperties.getResponseRegex());
    }

    @Bean
    public WebClient priceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
            .baseUrl(priceProperties.getRequestPath())
            .build();
    }

    @Bean
    public KafkaTemplate<String, PriceDto> priceKafkaTemplate() {
        return new KafkaTemplate<>(
            new DefaultKafkaProducerFactory<>(
                Map.of(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-broker-0:9091",
                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, new JsonSerializer<PriceDto>()
                )));
    }
}
