package com.slow3586.bettingplaftorm.betservice.bet;

import com.slow3586.bettingplaftorm.api.BetDto;
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
public class BetService {
    BetRepository betRepository;
    KafkaTemplate<String, BetDto> betKafkaProducer;
    BetMapper betMapper;

    public UUID save(BetDto betDto) {
        final BetEntity save = betRepository.save(betMapper.toEntity(betDto));
        betKafkaProducer.send("bet", betMapper.toDto(save));
        return save.getId();
    }
}
