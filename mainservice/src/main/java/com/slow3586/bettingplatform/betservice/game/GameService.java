package com.slow3586.bettingplatform.betservice.game;

import com.slow3586.bettingplatform.api.mainservice.GameDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class GameService {
    GameRepository gameRepository;
    GameMapper gameMapper;
    KafkaTemplate<String, Object> gameKafkaProducer;
    Random random = new Random();

    public List<GameDto> getCurrent() {
        return gameRepository.findUnfinished()
            .stream()
            .map(gameMapper::toDto)
            .toList();
    }

    public void save(GameDto gameDto) {
        final GameEntity save = gameRepository.save(gameMapper.toEntity(gameDto));
        gameKafkaProducer.send("game", gameMapper.toDto(save));
    }

    @Async
    @Scheduled(cron = "* * * * * *")
    public void startGame() {
        final LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        if (gameRepository.findUnfinished().isEmpty()) {
            this.save(GameDto.builder()
                .instrument("BTC")
                .createdAt(now)
                .startAt(now)
                .choice0("LONG")
                .choice1("SHORT")
                .status("new")
                .finishAt(now.plusSeconds(30 + random.nextInt(60)))
                .build());
        }
    }

    @Async
    @Scheduled(cron = "* * * * * *")
    public void finishGames() {
        gameRepository.findReadyToBeFinished()
            .stream()
            .map(gameMapper::toDto)
            .forEach(game -> {
                game.setFinished(true);
                this.save(game);
            });
    }
}
