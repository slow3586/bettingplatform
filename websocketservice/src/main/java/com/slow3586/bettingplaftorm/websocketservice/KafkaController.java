package com.slow3586.bettingplaftorm.websocketservice;

import com.slow3586.bettingplaftorm.websocketservice.client.BetServiceClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Controller;

@Controller
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public class KafkaController {
    RabbitTemplate rabbitTemplate;
    SimpMessagingTemplate simpMessagingTemplate;
    SimpUserRegistry simpUserRegistry;
    BetServiceClient betServiceClient;

    @KafkaListener(topics = "price.current")
    public void price(String value) {
        simpMessagingTemplate.convertAndSend(
            "/topic/price",
            value);
    }

    @KafkaListener(topics = "bet.created")
    public void bet(String value) {
        simpMessagingTemplate.convertAndSend(
            "/topic/bet",
            value);
    }

    @KafkaListener(topics = "game.started")
    public void game(String value) {
        simpMessagingTemplate.convertAndSend(
            "/topic/game",
            value);
    }
}
