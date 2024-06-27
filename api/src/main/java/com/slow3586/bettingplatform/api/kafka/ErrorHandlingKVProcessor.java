package com.slow3586.bettingplatform.api.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.processor.api.ContextualFixedKeyProcessor;
import org.apache.kafka.streams.processor.api.ContextualProcessor;
import org.apache.kafka.streams.processor.api.FixedKeyProcessorSupplier;
import org.apache.kafka.streams.processor.api.FixedKeyRecord;
import org.apache.kafka.streams.processor.api.ProcessorSupplier;
import org.apache.kafka.streams.processor.api.Record;
import org.springframework.kafka.support.KafkaHeaders;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.function.Function;

@Slf4j
public abstract class ErrorHandlingKVProcessor<VIn, VOut> extends ContextualProcessor<String, VIn, String, VOut> {
    public static <VIn, VOut> ProcessorSupplier<String, VIn, String, VOut> factory(
        Function<Record<String, VIn>, KeyValue<String, VOut>> function
    ) {
        return () -> new ErrorHandlingKVProcessor<>() {
            @Override
            public KeyValue<String, VOut> handle(Record<String, VIn> record) {
                return function.apply(record);
            }
        };
    }

    @Override
    public void process(Record<String, VIn> record) {
        try {
            log.info("#ErrorHandlingKVProcessor: {}", record);
            KeyValue<String, VOut> handle = this.handle(record);
            this.context().forward(record.withKey(handle.key).withValue(handle.value));
        } catch (Exception e) {
            log.error("#ErrorHandlingKVProcessor: {}", record, e);
            Record<String, VOut> badRecord = record
                .withKey(record.key())
                .withValue((VOut) null)
                .withHeaders(record.headers());
            badRecord.headers().add(KafkaHeaders.EXCEPTION_FQCN, e.getClass().getSimpleName().getBytes(StandardCharsets.UTF_8));
            badRecord.headers().add(KafkaHeaders.EXCEPTION_MESSAGE, e.getMessage().getBytes(StandardCharsets.UTF_8));
            this.context().forward(badRecord);
        }
    }

    abstract public KeyValue<String, VOut> handle(Record<String, VIn> record);
}
