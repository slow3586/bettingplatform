package com.slow3586.bettingplatform.auditservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchConfiguration;

@Configuration
public class ElasticConfiguration extends ReactiveElasticsearchConfiguration {
    @Value("${ELASTIC_PATH:localhost:9200}")
    private String elasticPath;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
            .connectedTo(elasticPath)
            .build();
    }
}
