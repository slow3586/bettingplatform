package com.slow3586.bettingplatform.api.auditservice;

import com.slow3586.bettingplatform.api.auditservice.dto.TraceDto;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.lang.reflect.UndeclaredThrowableException;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

@Aspect
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED)
@AuditDisabled
@EnableAspectJAutoProxy
public class AuditAspect {
    @Lazy final Tracer tracer;
    @Lazy final MeterRegistry meterRegistry;
    KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${KAFKA_BROKERS:localhost:9092}")
    String kafkaBrokers;
    Random random = new Random();

    @PostConstruct
    public void postConstruct() {
        kafkaTemplate = new KafkaTemplate<>(
            new DefaultKafkaProducerFactory<>(
                Map.of(
                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers
                ),
                new StringSerializer(),
                new JsonSerializer<>()));
    }

    @Pointcut("within(com.slow3586.bettingplatform..*)")
    public void app() {}

    @Pointcut("@within(org.springframework.stereotype.Service)")
    public void listener() {}

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void controller() {}

    @Around("app() && (listener() || controller())")
    protected Object joinPoint(@NonNull ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        if (random.nextInt(100) > 10) {
            return proceedingJoinPoint.proceed();
        }
        final Instant start = Instant.now();
        final TraceContext context = tracer == null ? null : tracer.currentTraceContext().context();
        final TraceDto.TraceDtoBuilder traceInfo = TraceDto.builder()
            .spanId(context == null ? null : context.spanId())
            .traceId(context == null ? null : context.traceId())
            .methodClass(proceedingJoinPoint.getSignature().getDeclaringTypeName())
            .methodName(proceedingJoinPoint.getSignature().getName())
            .methodArgs(Arrays.stream(proceedingJoinPoint.getArgs())
                .map(Objects::toString)
                .collect(Collectors.joining(", ")))
            .startTime(start);

        try {
            final Object result = proceedingJoinPoint.proceed();
            traceInfo.methodResult(Objects.toString(result));
            return result;
        } catch (final Throwable exceptionWrapper) {
            final Throwable exception =
                exceptionWrapper instanceof UndeclaredThrowableException
                    ? ((UndeclaredThrowableException) exceptionWrapper).getUndeclaredThrowable()
                    : exceptionWrapper;
            traceInfo.exceptionClass(exception.getClass().getName())
                .exceptionMessage(exception.getMessage())
                .exceptionStack(StringUtils.substring(
                    Arrays.toString(exception.getStackTrace()),
                    0,
                    8000));
            throw exception;
        } finally {
            final Instant end = Instant.now();
            kafkaTemplate.send(
                "trace",
                traceInfo
                    .endTime(end)
                    .duration(Duration.between(start, end))
                    .startTime(Instant.now())
                    .build());
        }
    }
}
