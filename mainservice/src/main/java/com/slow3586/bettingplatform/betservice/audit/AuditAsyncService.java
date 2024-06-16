package com.slow3586.bettingplatform.betservice.audit;

import com.slow3586.bettingplatform.api.TraceDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@AuditDisabled
public class AuditAsyncService {
    KafkaTemplate<String, Object> kafkaTemplate;

    @Async
    public void send(TraceDto.TraceDtoBuilder traceInfoBuilder) {
        kafkaTemplate.send(
            "trace",
            traceInfoBuilder
                .time(Instant.now())
                .build());
    }
}
