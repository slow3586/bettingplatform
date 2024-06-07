package com.slow3586.bettingplaftorm.websocketservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
@Service
public class WebSocketService {
    RabbitTemplate rabbitTemplate;
    SimpMessagingTemplate simpMessagingTemplate;
    SimpUserRegistry simpUserRegistry;

    @Bean
    public KStream<String, Double> kStream(StreamsBuilder streamsBuilder) {
        return streamsBuilder.stream(
                "price.current",
                Consumed.with(
                    Serdes.String(),
                    Serdes.Double()))
            .peek((k, v) ->
                simpMessagingTemplate.convertAndSend(
                    "/topic/price",
                    v));
    }

    @Bean
    public KStream<String, Boolean> kStream2(StreamsBuilder streamsBuilder) {
        return streamsBuilder.stream(
                "bet.created",
                Consumed.with(
                    Serdes.String(),
                    Serdes.Boolean()))
            .peek((k, v) ->
                simpMessagingTemplate.convertAndSend(
                    "/topic/bet",
                    v));
    }

    @Bean
    public KStream<String, Boolean> kStream3(StreamsBuilder streamsBuilder) {
        return streamsBuilder.stream(
                "game.started",
                Consumed.with(
                    Serdes.String(),
                    Serdes.Boolean()))
            .peek((k, v) ->
                simpMessagingTemplate.convertAndSend(
                    "/topic/game",
                    v));
    }
}
