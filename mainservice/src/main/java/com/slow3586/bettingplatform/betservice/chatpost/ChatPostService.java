package com.slow3586.bettingplatform.betservice.chatpost;

import com.slow3586.bettingplatform.api.mainservice.ChatPostDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class ChatPostService {
    ChatPostMapper chatPostMapper;
    ChatPostRepository chatPostRepository;
    KafkaTemplate<String, Object> kafkaProducer;

    public UUID save(ChatPostDto chatPostDto) {
        final ChatPostEntity save = chatPostRepository.save(chatPostMapper.toEntity(chatPostDto));
        kafkaProducer.send("chat_post",
            String.valueOf(save.getId()),
            chatPostMapper.toDto(save));
        return save.getId();
    }

    public List<ChatPostDto> getLatest() {
        return chatPostRepository.findLatest()
            .stream()
            .map(chatPostMapper::toDto)
            .toList();
    }
}
