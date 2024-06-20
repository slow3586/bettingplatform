package com.slow3586.bettingplatform.auditservice.trace;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "trace")
public class TraceEntity {
    @Id
    UUID id;
    Instant startTime;
    Instant endTime;
    Duration duration;
    String spanId;
    String traceId;
    String methodClass;
    String methodName;
    String methodArgs;
    String methodResult;
    String exceptionClass;
    String exceptionMessage;
    String exceptionStack;
}
