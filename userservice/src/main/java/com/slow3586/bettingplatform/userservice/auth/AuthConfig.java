package com.slow3586.bettingplatform.userservice.auth;

import com.slow3586.bettingplatform.api.userservice.dto.AuthDto;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class AuthConfig {
    AuthProperties authProperties;
    @NonFinal
    @Value("${KAFKA_BROKERS:localhost:9092}")
    String kafkaBrokers;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4, new SecureRandom(new byte[]{1, 2, 3}));
    }

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(authProperties.getToken()));
    }

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "user-service-streams");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public KStream<String, AuthDto> authStream(StreamsBuilder streamsBuilder) {
        final JsonSerde<AuthDto> valueSerde = new JsonSerde<>(AuthDto.class);
        valueSerde.deserializer().addTrustedPackages("*");
        valueSerde.deserializer().ignoreTypeHeaders();

        final KStream<String, AuthDto> stream = streamsBuilder.stream(
                "user-service.public.auth",
                Consumed.with(
                    Serdes.String(),
                    valueSerde))
            .selectKey((k, v) -> v.getLogin());

        stream.mapValues(AuthDto::getUserId)
            .mapValues(u -> Objects.toString(u))
            .to("user-service.table.auth.login.user_id");

        stream.mapValues(AuthDto::getPassword)
            .to("user-service.table.auth.login.password");

        streamsBuilder.globalTable("user-service.table.auth.login.user_id",
            Materialized.as("user-service.table.auth.login.user_id"));
        streamsBuilder.globalTable("user-service.table.auth.login.password",
            Materialized.as("user-service.table.auth.login.password"));

        return stream;
    }
}
