package com.slow3586.bettingplatform.betservice.chatpost;

import com.slow3586.bettingplatform.api.mainservice.dto.ChatPostDto;
import com.slow3586.bettingplatform.api.mainservice.dto.ChatPostRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("chat_post")
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class ChatPostRest {
    ChatPostService chatPostService;

    @GetMapping(value = "latest")
    public List<ChatPostDto> getLatest() {
        return chatPostService.getLatest();
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public UUID make(ChatPostRequest chatPostRequest) {
        return chatPostService.make(chatPostRequest);
    }
}
