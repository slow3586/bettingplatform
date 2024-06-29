package com.slow3586.bettingplatform.betservice.gametype;

import com.slow3586.bettingplatform.api.mainservice.dto.GameTypeDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class GameTypeService {
    GameTypeRepository gameTypeRepository;
    GameTypeMapper gameTypeMapper;
    KafkaTemplate<String, Object> kafkaTemplate;

    public GameTypeDto getByName(String name) {
        return gameTypeRepository.findByName(name)
            .map(gameTypeMapper::toDto)
            .orElseThrow();
    }
}
