package com.slow3586.bettingplatform.auditservice.metric;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MetricRepository extends MongoRepository<MetricEntity, UUID> {
}
