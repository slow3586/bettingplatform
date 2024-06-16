package com.slow3586.bettingplatform.betservice.audit;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class AuditService {
    private final MeterRegistry meterRegistry;
    KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(cron = "*/15 * * * * *")
    public void waetawe() {
        meterRegistry.forEachMeter((m) ->
            kafkaTemplate.send("metric", m)
        );
    }
}
