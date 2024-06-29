package com.slow3586.bettingplatform.betservice.gameinstance;

import com.slow3586.bettingplatform.api.mainservice.dto.GameInstanceDto;
import com.slow3586.bettingplatform.api.mainservice.dto.GameInstanceRegisterRequest;
import com.slow3586.bettingplatform.api.mainservice.dto.GameInstanceUpdateRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class GameInstanceService {
    GameInstanceRepository gameInstanceRepository;
    GameInstanceMapper gameInstanceMapper;

    @KafkaListener(topics = "game_instance.register")
    public void register(GameInstanceRegisterRequest gameInstanceRegisterRequest) {
        gameInstanceRepository.save();
    }

    @KafkaListener(topics = "game_instance.update")
    public void update(GameInstanceUpdateRequest gameInstanceUpdateRequest) {
        gameInstanceRepository.save();
    }

    public List<GameInstanceDto> getCurrent(String gameName) {
        return gameInstanceRepository.findUnfinished();
    }
}
