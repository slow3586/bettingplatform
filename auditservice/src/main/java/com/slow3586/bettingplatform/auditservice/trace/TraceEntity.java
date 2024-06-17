package com.slow3586.bettingplatform.auditservice.trace;

import com.slow3586.bettingplatform.api.MongoUuidEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("trace")
public class TraceEntity implements MongoUuidEntity {
    @Id
    UUID id;
    Instant time;
    String serviceName;
    String hostName;
    String spanId;
    String traceId;
    String eventId;
    String methodClass;
    String methodName;
    String methodArgs;
    String methodResult;
    String exceptionClass;
    String exceptionMessage;
    String exceptionStack;
}
