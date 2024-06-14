package com.slow3586.bettingplatform.websocketservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;

@SpringBootApplication
@EnableCaching
@EnableWebSecurity
@EnableWebSocketSecurity
@EnableMethodSecurity
@EnableScheduling
@EnableFeignClients(basePackages = "com.slow3586.bettingplatform.api")
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@EnableKafka
public class WebSocketServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebSocketServiceApplication.class, args);
    }

}
