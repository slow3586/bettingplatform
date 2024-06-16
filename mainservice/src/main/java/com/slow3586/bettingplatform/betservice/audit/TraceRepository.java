package com.slow3586.bettingplatform.betservice.audit;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@AuditDisabled
public interface TraceRepository extends CrudRepository<TraceEntity, UUID> {
}
