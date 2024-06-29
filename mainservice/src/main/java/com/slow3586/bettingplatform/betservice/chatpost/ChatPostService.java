package com.slow3586.bettingplatform.betservice.chatpost;

import com.slow3586.bettingplatform.api.mainservice.dto.ChatPostDto;
import com.slow3586.bettingplatform.api.mainservice.dto.ChatPostRegisterRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class ChatPostService {
    ChatPostMapper chatPostMapper;
    ChatPostRepository chatPostRepository;
    KafkaTemplate<String, Object> kafkaTemplate;

    public List<ChatPostDto> getLatest() {
        return chatPostRepository.findLatest()
            .stream()
            .map(chatPostMapper::toDto)
            .toList();
    }

    @KafkaListener(topics = "chat_post.register")
    public void register(
        @Header(KafkaHeaders.RECEIVED_KEY) String userName,
        ChatPostRegisterRequest chatPostRequest
    ) {
        chatPostRepository.save(
            ChatPostEntity.builder()
                .userName(userName)
                .createdAt(Instant.now())
                .typeId(chatPostRequest.getTypeId())
                .build());
    }
}
