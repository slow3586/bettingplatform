package com.slow3586.bettingplatform.auditservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.kafka.annotation.EnableKafkaStreams;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties
@EnableKafkaStreams
@EnableElasticsearchRepositories
public class AuditServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(com.slow3586.bettingplatform.auditservice.AuditServiceApplication.class, args);
    }

}
