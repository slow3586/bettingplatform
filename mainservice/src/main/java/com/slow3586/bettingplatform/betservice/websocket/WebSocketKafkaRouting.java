package com.slow3586.bettingplatform.betservice.websocket;

import com.slow3586.bettingplatform.api.mainservice.dto.BetDto;
import com.slow3586.bettingplatform.api.mainservice.dto.ChatPostDto;
import com.slow3586.bettingplatform.api.mainservice.dto.GameTypeDto;
import com.slow3586.bettingplatform.api.mainservice.dto.PriceGameDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public class WebSocketKafkaRouting {
    SimpMessagingTemplate messagingTemplate;

    @KafkaListener(topics = "price")
    public void price(PriceGameDto dto) {
        messagingTemplate.convertAndSend("/topic/gamestat", dto);
    }

    @KafkaListener(topics = "bet")
    public void bet(BetDto dto) {
        messagingTemplate.convertAndSend("/topic/bet", dto);
    }

    @KafkaListener(topics = "game")
    public void game(GameTypeDto dto) {
        messagingTemplate.convertAndSend("/topic/gametype", dto);
    }

    @KafkaListener(topics = "chat_post")
    public void chat(ChatPostDto dto) {
        messagingTemplate.convertAndSend("/topic/chat_post", dto);
    }
}
