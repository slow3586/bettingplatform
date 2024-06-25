package com.slow3586.bettingplatform.api.auditservice;

import com.slow3586.bettingplatform.api.auditservice.dto.MetricDto;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    @Around("@within(org.springframework.web.bind.annotation.RestController) " +
        "&& within(com.slow3586.bettingplatform..*) " +
        "&& !@within(com.slow3586.bettingplatform.api.auditservice.AuditDisabled)" +
        "&& !@within(org.springframework.scheduling.annotation.Scheduled)")
    protected Object joinPoint(@NonNull ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
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
        } catch (final Throwable exception) {
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

    @Scheduled(cron = "*/15 * * * * *")
    public void sendMetrics() {
        final HashMap<String, Double> values = new HashMap<>();

        meterRegistry.forEachMeter((m) ->
            StreamSupport.stream(m.measure().spliterator(), false)
                .forEach(d -> {
                    final String tags = m.getId()
                        .getTags()
                        .stream()
                        .map(t -> t.getKey() + "=\"" + t.getValue() + "\"")
                        .collect(Collectors.joining(","));
                    final String type = d.getStatistic().getTagValueRepresentation();
                    final String baseUnit = m.getId().getBaseUnit();
                    final String key = m.getId().getName()
                        + (StringUtils.isBlank(baseUnit) ? "" : "_" + baseUnit)
                        + (StringUtils.isBlank(type) ? "" : "_" + type)
                        + (StringUtils.isBlank(tags) ? "" : "{" + tags + "}");
                    values.put(StringUtils.replace(key, ".", "_"), d.getValue());
                }));

        kafkaTemplate.send(
            "metric",
            MetricDto.builder()
                .time(Instant.now())
                .values(values)
                .build());
    }
}
