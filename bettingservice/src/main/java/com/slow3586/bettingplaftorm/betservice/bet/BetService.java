package com.slow3586.bettingplaftorm.betservice.bet;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterators;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class BetService {
    BetRepository betRepository;
    KafkaTemplate<String, Boolean> kafkaProducer;

    public List<BetEntity> get(
        final UUID game,
        final UUID player
    ) {
        return betRepository.findAll();
    }

    public void make(UUID user, Boolean value) {
        betRepository.save(
            BetEntity.builder()
                .owner(user)
                .build());
    }

    @Async
    @Scheduled(cron = "* * * * *")
    public void scheduled() {
    }
}
