package com.slow3586.bettingplatform.userservice.auth;

import com.slow3586.bettingplatform.api.userservice.dto.AuthDto;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.internals.RocksDBKeyValueBytesStoreSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;
import java.security.SecureRandom;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class AuthConfig {
    AuthProperties authProperties;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(4, new SecureRandom(new byte[]{1, 2, 3}));
    }

    @Bean
    public SecretKey secretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(authProperties.getToken()));
    }

    @Bean
    public StreamsBuilder authStream(StreamsBuilder streamsBuilder) {
        final JsonSerde<AuthDto> valueSerde = new JsonSerde<>(AuthDto.class);
        valueSerde.deserializer().addTrustedPackages("*");
        valueSerde.deserializer().ignoreTypeHeaders();

        Materialized<String, AuthDto, KeyValueStore<Bytes, byte[]>> loginUserId =
            Materialized.as(
                new RocksDBKeyValueBytesStoreSupplier(
                    "USER_SERVICE_AUTH_BY_LOGIN",
                    true));
        streamsBuilder.globalTable("USER_SERVICE_AUTH_BY_LOGIN",
            loginUserId
                .withKeySerde(Serdes.String())
                .withValueSerde(valueSerde));

        return streamsBuilder;
    }
}
