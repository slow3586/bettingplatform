package com.slow3586.bettingplaftorm.betservice.bet;

import com.slow3586.bettingplaftorm.api.BetMakeRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class BetService {
    BetRepository betRepository;
    KafkaTemplate<String, String> kafkaProducer;

    public List<BetEntity> get(
        final UUID game,
        final UUID player
    ) {
        return betRepository.findAll();
    }

    public UUID make(BetMakeRequest betMakeRequest) {
        return betRepository.save(
            BetEntity.builder()
                .userId(betMakeRequest.getUserId())
                .typeId(betMakeRequest.getBetTypeId())
                .value(betMakeRequest.getValue())
                .build()
        ).getId();
    }
}
