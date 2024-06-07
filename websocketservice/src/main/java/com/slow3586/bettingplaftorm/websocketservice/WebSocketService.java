package com.slow3586.bettingplaftorm.websocketservice;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public class WebSocketService {
    RabbitTemplate rabbitTemplate;
    SimpMessagingTemplate simpMessagingTemplate;
    SimpUserRegistry simpUserRegistry;

    @Autowired
    public void kStream(StreamsBuilder streamsBuilder) {
        streamsBuilder.stream(
                "price.current",
                Consumed.with(
                    Serdes.String(),
                    Serdes.String()))
            .peek((k, v) ->
                simpMessagingTemplate.convertAndSend(
                    "/topic/price",
                    v));
    }

    @Autowired
    public void kStream2(StreamsBuilder streamsBuilder) {
        streamsBuilder.stream(
                "bet.created",
                Consumed.with(
                    Serdes.String(),
                    Serdes.String()))
            .peek((k, v) ->
                simpMessagingTemplate.convertAndSend(
                    "/topic/bet",
                    v));
    }

    /*
    @KafkaListener(topics = "game.started", groupId = "websocket")
    public void kStream3(Boolean value) {
        simpMessagingTemplate.convertAndSend(
            "/topic/game",
            value);
    }

     */
}
