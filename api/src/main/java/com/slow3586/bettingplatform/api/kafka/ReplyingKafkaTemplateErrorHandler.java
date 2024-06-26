package com.slow3586.bettingplatform.api.kafka;


import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReplyingKafkaTemplateErrorHandler implements ConsumerAwareListenerErrorHandler {
    @Override
    public Object handleError(
        Message<?> message,
        ListenerExecutionFailedException exception,
        Consumer<?, ?> consumer
    ) {
        log.error("#handle", exception);
        return MessageBuilder.fromMessage(message)
            .setHeader("exception.class", exception.getCause().getClass().getSimpleName())
            .setHeader("exception.message", exception.getCause().getMessage())
            .build();
    }
}
