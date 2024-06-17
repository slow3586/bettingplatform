package com.slow3586.bettingplatform.betservice.audit;

import com.slow3586.bettingplatform.api.auditservice.MetricDto;
import com.slow3586.bettingplatform.api.auditservice.TraceDto;
import com.slow3586.bettingplatform.api.mainservice.BetDto;
import com.slow3586.bettingplatform.api.mainservice.ChatPostDto;
import com.slow3586.bettingplatform.api.mainservice.GameDto;
import com.slow3586.bettingplatform.api.mainservice.PriceDto;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.DelegatingByTopicSerializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class AuditService {
    @Lazy
    MeterRegistry meterRegistry;
    @Lazy
    KafkaTemplate<String, Object> kafkaTemplate;

    @Async
    public void sendTraceAsync(TraceDto.TraceDtoBuilder traceInfoBuilder) {
        kafkaTemplate.send(
            "trace",
            traceInfoBuilder
                .time(Instant.now())
                .build());
    }

    @Scheduled(cron = "*/15 * * * * *")
    public void sendMetrics() {
        final HashMap<String, Double> values = new HashMap<>();

        meterRegistry.forEachMeter((m) ->
            StreamSupport.stream(m.measure().spliterator(), false)
                .forEach(d -> values.put
                    (StringUtils.replace(m.getId().getName(), ".", "_")
                            + "_" + m.getId().getBaseUnit()
                            + "_" + d.getStatistic().getTagValueRepresentation(),
                        d.getValue())));

        kafkaTemplate.send(
            "metric",
            MetricDto.builder()
                .serviceName("main")
                .time(Instant.now())
                .values(values)
                .build());
    }
}
