package com.slow3586.bettingplatform.api.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import reactor.core.publisher.Mono;

public class KafkaRestUtils {
    public static Mono<Object> sendAndReceive(
        ReplyingKafkaTemplate<String, Object, Object> kafkaTemplate,
        String topic,
        Object record
    ) {
        return Mono.fromFuture(
                kafkaTemplate.sendAndReceive(
                    new ProducerRecord<>(
                        topic,
                        record)))
            .map(ConsumerRecord::value);
    }
}
