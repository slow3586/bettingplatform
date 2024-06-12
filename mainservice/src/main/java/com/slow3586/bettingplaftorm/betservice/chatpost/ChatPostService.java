package com.slow3586.bettingplaftorm.betservice.chatpost;

import com.slow3586.bettingplaftorm.api.ChatPostDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class ChatPostService {
    ChatPostMapper chatPostMapper;
    ChatPostRepository chatPostRepository;
    KafkaTemplate<String, ChatPostDto> kafkaProducer;

    public UUID save(ChatPostDto chatPostDto) {
        final ChatPostEntity save = chatPostRepository.save(chatPostMapper.toEntity(chatPostDto));
        kafkaProducer.send("price_game", chatPostMapper.toDto(save));
        return save.getId();
    }
}
