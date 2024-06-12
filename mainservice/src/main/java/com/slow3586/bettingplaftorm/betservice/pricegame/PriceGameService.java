package com.slow3586.bettingplaftorm.betservice.pricegame;

import com.slow3586.bettingplaftorm.api.PriceGameDto;
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
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class PriceGameService {
    PriceGameRepository priceGameRepository;
    PriceGameMapper priceGameMapper;
    KafkaTemplate<String, PriceGameDto> gameKafkaProducer;
    Random random = new Random();

    public void save(PriceGameDto priceGameDto) {
        final PriceGameEntity save = priceGameRepository.save(priceGameMapper.toEntity(priceGameDto));
        gameKafkaProducer.send("price_game", priceGameMapper.toDto(save));
    }

    @Async
    @Scheduled(cron = "* * * * * *")
    public void startGame() {
        final LocalDateTime now = LocalDateTime.now(ZoneId.of("UTC"));
        this.save(PriceGameDto.builder()
            .instrument("BTC")
            .dateStarted(now)
            .dateFinished(now
                .plusSeconds(30 + random.nextInt(60)))
            .build());
    }

    @Async
    @Scheduled(cron = "* * * * * *")
    public void finishGames() {
        priceGameRepository.findReadyToBeFinished()
            .stream()
            .map(priceGameMapper::toDto)
            .forEach(game -> {
                game.setFinished(true);
                this.save(game);
            });
    }
}
