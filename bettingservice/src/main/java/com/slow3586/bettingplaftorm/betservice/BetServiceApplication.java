package com.slow3586.bettingplaftorm.betservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableScheduling
@EnableAsync
@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties
@EnableKafka
public class BetServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BetServiceApplication.class, args);
    }

}
