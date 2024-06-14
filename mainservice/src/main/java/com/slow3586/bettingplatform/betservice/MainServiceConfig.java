package com.slow3586.bettingplatform.betservice;

import com.slow3586.bettingplatform.api.mainservice.BetDto;
import com.slow3586.bettingplatform.api.mainservice.ChatPostDto;
import com.slow3586.bettingplatform.api.mainservice.GameDto;
import com.slow3586.bettingplatform.api.mainservice.PriceDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.DelegatingByTopicSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.Map;
import java.util.regex.Pattern;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class MainServiceConfig {
    MainServiceProperties mainServiceProperties;
    MainServiceSecurityWebFilter securityWebFilter;

    @Bean
    public DefaultSecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .logout(l -> l.logoutUrl("/logout"))
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .securityContext(c -> c.securityContextRepository(
                new HttpSessionSecurityContextRepository()))
            .addFilterBefore(securityWebFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(
            new DefaultKafkaProducerFactory<>(
                Map.of(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, mainServiceProperties.getKafkaBroker()
                ),
                new StringSerializer(),
                new DelegatingByTopicSerializer(Map.of(
                    Pattern.compile("bet"), new JsonSerializer<BetDto>(),
                    Pattern.compile("price"), new JsonSerializer<PriceDto>(),
                    Pattern.compile("game"), new JsonSerializer<GameDto>(),
                    Pattern.compile("chat_post"), new JsonSerializer<ChatPostDto>()
                ), new StringSerializer())));
    }

}
