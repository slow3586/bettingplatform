package com.slow3586.bettingplatform.betservice.bet;

import com.slow3586.bettingplatform.api.mainservice.dto.BetDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class BetService {
    BetRepository betRepository;
    BetMapper betMapper;

    public List<BetDto> getByUser(String userLogin) {
        return betRepository.findAllByUserLogin(userLogin)
            .stream()
            .map(betMapper::toDto)
            .toList();
    }

    @KafkaListener(topics = "bet.process")
    protected UUID process(BetDto betDto) {
        final BetEntity save = betRepository.save(betMapper.toEntity(betDto));
        return save.getId();
    }
}
