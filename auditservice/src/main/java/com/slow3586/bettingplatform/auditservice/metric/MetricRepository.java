package com.slow3586.bettingplatform.auditservice.metric;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MetricRepository extends ElasticsearchRepository<MetricEntity, UUID> {
}
