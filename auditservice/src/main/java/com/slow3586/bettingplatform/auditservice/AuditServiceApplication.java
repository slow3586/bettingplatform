package com.slow3586.bettingplatform.auditservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties
@EnableKafkaStreams
@EnableMongoRepositories
public class AuditServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.slow3586.bettingplatform.auditservice.AuditServiceApplication.class, args);
    }

}
