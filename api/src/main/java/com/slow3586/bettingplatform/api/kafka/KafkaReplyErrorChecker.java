package com.slow3586.bettingplatform.api.kafka;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
@Slf4j
public class KafkaReplyErrorChecker implements Function<ConsumerRecord<?, ?>, Exception> {
    @Override
    public Exception apply(ConsumerRecord<?, ?> record) {
        try {
            final String excClass = Optional.ofNullable(
                    record.headers().lastHeader("exception.class"))
                .map(Header::value)
                .map(String::new)
                .orElse(null);
            final String excMessage = Optional.ofNullable(
                    record.headers().lastHeader("exception.message"))
                .map(Header::value)
                .map(String::new)
                .orElse("Unknown");
            if (excClass != null) {
                if ("IllegalArgumentException".equals(excClass)) {
                    return new IllegalArgumentException(excMessage);
                }
                return new RuntimeException(excClass + ": " + excMessage);
            }
            return null;
        } catch (Exception e) {
            log.error("#error checker", e);
            throw e;
        }
    }
}
