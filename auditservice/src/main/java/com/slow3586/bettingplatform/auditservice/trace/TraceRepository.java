package com.slow3586.bettingplatform.auditservice.trace;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TraceRepository extends MongoRepository<TraceEntity, UUID> {
}
