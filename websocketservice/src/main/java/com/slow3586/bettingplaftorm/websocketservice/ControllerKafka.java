package com.slow3586.bettingplaftorm.websocketservice;

import com.slow3586.bettingplaftorm.api.BetDto;
import com.slow3586.bettingplaftorm.api.PriceDto;
import com.slow3586.bettingplaftorm.api.PriceGameDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public class ControllerKafka {
    @KafkaListener(topics = "price")
    @SendTo("/topic/price")
    public PriceDto price(PriceDto dto) {
        return dto;
    }

    @KafkaListener(topics = "bet")
    @SendTo("/topic/bet")
    public BetDto bet(BetDto dto) {
        return dto;
    }

    @KafkaListener(topics = "price_game")
    @SendTo("/topic/price_game")
    public PriceGameDto game(PriceGameDto dto) {
        return dto;
    }

    @KafkaListener(topics = "chat_post")
    @SendTo("/topic/chat_post")
    public PriceGameDto chat(PriceGameDto dto) {
        return dto;
    }
}
