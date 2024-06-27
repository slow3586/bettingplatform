package com.slow3586.bettingplatform.userservice.auth;

import com.slow3586.bettingplatform.api.kafka.ErrorHandlingKVProcessor;
import com.slow3586.bettingplatform.api.kafka.ErrorHandlingVProcessor;
import com.slow3586.bettingplatform.api.kafka.KafkaStreamsUtils;
import com.slow3586.bettingplatform.api.userservice.dto.AuthDto;
import com.slow3586.bettingplatform.api.userservice.dto.LoginRequest;
import com.slow3586.bettingplatform.userservice.customer.CustomerRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Branched;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class AuthEvents {
    AuthRepository authRepository;
    CustomerRepository customerRepository;
    AuthProperties authProperties;
    PasswordEncoder passwordEncoder;
    SecretKey secretKey;
    @Qualifier("defaultKafkaStreamsBuilder") StreamsBuilder streamsBuilder;

    @Bean
    public KTable<String, AuthDto> authDtoKTable() {
        final JsonSerde<AuthDto> authDtoJsonSerde = new JsonSerde<>(AuthDto.class);
        authDtoJsonSerde.deserializer().addTrustedPackages("*");
        return streamsBuilder.table("user-service.public.auth", Materialized.with(Serdes.String(), authDtoJsonSerde));
    }

    @Bean
    public StreamsBuilder loginStream() {
        streamsBuilder.stream("auth.request.login", KafkaStreamsUtils.consumed(LoginRequest.class))
            .leftJoin(authDtoKTable(), KeyValue::pair)
            .processValues(ErrorHandlingVProcessor.factory(
                (record) -> {
                    final LoginRequest loginRequest = record.value().key;
                    final AuthDto authDto = record.value().value;

                    if (authDto == null
                        || !passwordEncoder.matches(
                        loginRequest.getPassword(),
                        authDto.getPassword())) {

                        throw new IllegalArgumentException("Incorrect login or password!");
                    }

                    return Jwts.builder()
                        .subject(authDto.getLogin())
                        .expiration(Date.from(
                            Instant.now().plus(
                                Duration.ofMinutes(
                                    authProperties.getExpirationMinutes()))))
                        .signWith(secretKey)
                        .compact();
                }
            )).to("auth.response", KafkaStreamsUtils.produced(String.class));

        return streamsBuilder;
    }

    @Bean
    public StreamsBuilder tokenStream() {
        streamsBuilder.stream("auth.request.token", KafkaStreamsUtils.consumed(String.class))
            .processValues(ErrorHandlingVProcessor.factory(
                (record) -> Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(record.value())
                    .getPayload()))
            .process(ErrorHandlingKVProcessor.factory(
                (record) -> new KeyValue<>(
                    record.value() != null ? record.value().getSubject() : null,
                    record.value())))
            .split()
            .branch((k, v) -> k == null || v == null,
                Branched.withConsumer(stream -> stream.to("auth.response")))
            .defaultBranch(Branched.withConsumer(stream ->
                stream.leftJoin(authDtoKTable(), KeyValue::pair)
                    .processValues(ErrorHandlingVProcessor.factory(
                        (record) -> {
                            final Claims claims = record.value().key;
                            final AuthDto authDto = record.value().value;

                            if (authDto == null) {
                                throw new IllegalArgumentException("Unknown user!");
                            }

                            if (Instant.now().isBefore(claims.getExpiration().toInstant())) {
                                throw new IllegalArgumentException("Token expired!");
                            }

                            return claims.getSubject();
                        }
                    )).to("auth.response", KafkaStreamsUtils.produced(String.class))));

        return streamsBuilder;
    }

}
