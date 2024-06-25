package com.slow3586.bettingplatform.api.auditservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.Instant;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class TraceDto {
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
