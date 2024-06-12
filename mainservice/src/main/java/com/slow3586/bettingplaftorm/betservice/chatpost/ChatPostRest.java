package com.slow3586.bettingplaftorm.betservice.chatpost;

import com.slow3586.bettingplaftorm.api.ChatPostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController(value = "chat_post")
@RequiredArgsConstructor
public class ChatPostRest {
    ChatPostService chatPostService;

    @PostMapping
    public UUID make(ChatPostDto chatPostDto) {
        return chatPostService.save(chatPostDto);
    }
}
