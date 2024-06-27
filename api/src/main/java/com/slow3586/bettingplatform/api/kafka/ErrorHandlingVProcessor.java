package com.slow3586.bettingplatform.api.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.processor.api.ContextualFixedKeyProcessor;
import org.apache.kafka.streams.processor.api.FixedKeyProcessorSupplier;
import org.apache.kafka.streams.processor.api.FixedKeyRecord;
import org.springframework.kafka.support.KafkaHeaders;

import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@Slf4j
public abstract class ErrorHandlingVProcessor<VIn, VOut> extends ContextualFixedKeyProcessor<String, VIn, VOut> {
    public static <VIn, VOut> FixedKeyProcessorSupplier<String, VIn, VOut> factory(
        Function<FixedKeyRecord<String, VIn>, VOut> function
    ) {
        return () -> new ErrorHandlingVProcessor<>() {
            @Override
            public VOut handle(FixedKeyRecord<String, VIn> record) {
                return function.apply(record);
            }
        };
    }

    @Override
    public void process(FixedKeyRecord<String, VIn> record) {
        try {
            //log.info("#ErrorHandlingVProcessor: {}", record);
            this.context().forward(record.withValue(this.handle(record)));
        } catch (Exception e) {
            log.error("#ErrorHandlingKVProcessor: {}", record, e);
            FixedKeyRecord<String, VOut> badRecord = record.withValue((VOut) null).withHeaders(record.headers());
            badRecord.headers().add(KafkaHeaders.EXCEPTION_FQCN, e.getClass().getSimpleName().getBytes(StandardCharsets.UTF_8));
            badRecord.headers().add(KafkaHeaders.EXCEPTION_MESSAGE, e.getMessage().getBytes(StandardCharsets.UTF_8));
            this.context().forward(badRecord);
        }
    }

    abstract public VOut handle(FixedKeyRecord<String, VIn> record);
}
