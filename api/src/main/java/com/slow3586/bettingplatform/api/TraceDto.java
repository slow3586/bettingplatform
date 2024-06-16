package com.slow3586.bettingplatform.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class TraceDto {
    Instant time;
    String serviceName;
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
