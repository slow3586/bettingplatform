package com.slow3586.bettingplatform.betservice.audit;

import com.slow3586.bettingplatform.api.TraceDto;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Aspect
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class AuditAspect {
    static String SERVICE_NAME = "main";
    static String TOPIC_NAME = "trace";
    @Lazy
    KafkaTemplate<String, Object> kafkaTemplate;
    @Lazy
    Tracer tracer;

    @Pointcut("within(com.slow3586.bettingplatform..*)")
    public void anyThisApp() {
    }

    @Pointcut("execution(public * *(..))")
    public void anyPublic() {
    }

    @Pointcut("!within(is(EnumType))" +
        "&& !within(is(FinalType))")
    public void notFinal() {
    }

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void anyComponent() {
    }

    @Pointcut("anyPublic() && anyThisApp() && anyComponent()")
    public void fullTracing() {
    }

    @Around("fullTracing()")
    public Object joinPoint(@NonNull ProceedingJoinPoint joinPoint) throws Throwable {
        final TraceDto.TraceInfoBuilder traceInfo = getTraceInfo(joinPoint);

        final Object result;
        kafkaTemplate.send(
            "trace.in",
            traceInfo
                .time(Instant.now())
                .build());

        try {
            result = joinPoint.proceed();
        } catch (Throwable exception) {
            kafkaTemplate.send(
                "trace.err",
                traceInfo
                    .time(Instant.now())
                    .exceptionClass(exception.getClass().getSimpleName())
                    .exceptionMessage(exception.getMessage())
                    .exceptionStack(Arrays.stream(exception.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.joining(", ")))
                    .build());
            throw exception;
        }

        kafkaTemplate.send(
            "trace.out",
            traceInfo
                .time(Instant.now())
                .methodResult(Objects.toString(result))
                .build());
        return result;
    }

    //@AfterThrowing(pointcut = "!fullTracing() && notFinal()", throwing = "exception")
    public void afterThrowing(JoinPoint joinPoint, Throwable exception) throws Throwable {
        final TraceDto.TraceInfoBuilder traceInfo = getTraceInfo(joinPoint);

        kafkaTemplate.send(
            "trace.err",
            traceInfo
                .time(Instant.now())
                .exceptionClass(exception.getClass().getSimpleName())
                .exceptionMessage(exception.getMessage())
                .build());
        throw exception;
    }

    protected TraceDto.TraceInfoBuilder getTraceInfo(JoinPoint joinPoint) {
        final TraceContext context = tracer == null ? null : tracer.currentTraceContext().context();
        final String spanId = context == null ? null : context.spanId();
        final String traceId = context == null ? null : context.traceId();
        final String methodArgs = Arrays.stream(joinPoint.getArgs())
            .map(Object::toString)
            .collect(Collectors.joining(", "));
        final String methodName = joinPoint.getSignature().getName();
        final String methodClass = joinPoint.getSignature().getDeclaringTypeName();
        return TraceDto.builder()
            .serviceName(SERVICE_NAME)
            .spanId(spanId)
            .traceId(traceId)
            .methodClass(methodClass)
            .methodName(methodName)
            .methodArgs(methodArgs);
    }
}
