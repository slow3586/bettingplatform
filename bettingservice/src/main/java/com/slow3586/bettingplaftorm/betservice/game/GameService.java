package com.slow3586.bettingplaftorm.betservice.game;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class GameService {
    GameRepository gameRepository;
    GameEntityMapper gameEntityMapper;
    KafkaTemplate<String, String> kafkaProducer;
    Random random = new Random();

    public GameEntityDto get(UUID uuid) {
        return gameRepository.findById(uuid)
            .map(gameEntityMapper::toDto)
            .orElseThrow();
    }

    @Async
    @Scheduled(cron = "* * * * *")
    public void startGame() {
        final int seconds = 30 + random.nextInt(60);
        final GameEntity game = GameEntity.builder()
            .instrument("BTC")
            .dateStarted(LocalDateTime.now(ZoneId.of("UTC")))
            .length(Duration.ofSeconds(seconds))
            .dateFinished(LocalDateTime.now(ZoneId.of("UTC")).plusSeconds(seconds))
            .build();
        gameRepository.save(game);
        kafkaProducer.send("game.started", String.valueOf(game.getId()));
    }


    @Async
    @Scheduled(cron = "* * * * *")
    public void finishGames() {
        gameRepository.findAllByFinishedBeforeAndFinished(new Date(), false)
            .forEach(game -> {
                game.setFinished(true);
                gameRepository.save(game);
                kafkaProducer.send("game.finished", String.valueOf(game.getId()));
            });
    }
}
