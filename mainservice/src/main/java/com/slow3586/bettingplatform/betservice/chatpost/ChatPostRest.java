package com.slow3586.bettingplatform.betservice.chatpost;

import com.slow3586.bettingplatform.api.SecurityUtils;
import com.slow3586.bettingplatform.api.mainservice.dto.ChatPostDto;
import com.slow3586.bettingplatform.api.mainservice.dto.ChatPostRegisterRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("chat_post")
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class ChatPostRest {
    ChatPostService chatPostService;
    KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping()
    public List<ChatPostDto> getLatest() {
        return chatPostService.getLatest();
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(security = @SecurityRequirement(name = "BearerAuth"))
    public void register(ChatPostRegisterRequest chatPostRegisterRequest) {
        kafkaTemplate.send("chat_post.register",
            SecurityUtils.getPrincipalId(),
            chatPostRegisterRequest);
    }
}
