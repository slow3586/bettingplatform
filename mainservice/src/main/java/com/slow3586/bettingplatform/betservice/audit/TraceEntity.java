package com.slow3586.bettingplatform.betservice.audit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("trace")
public class TraceEntity {
    Instant time;
    String serviceName;
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
