package com.slow3586.bettingplatform.auditservice.trace;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TraceRepository extends ElasticsearchRepository<TraceEntity, UUID> {
}
