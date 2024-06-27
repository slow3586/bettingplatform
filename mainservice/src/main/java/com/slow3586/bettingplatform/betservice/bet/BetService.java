package com.slow3586.bettingplatform.betservice.bet;

import com.slow3586.bettingplatform.api.SecurityUtils;
import com.slow3586.bettingplatform.api.mainservice.dto.BetDto;
import com.slow3586.bettingplatform.api.mainservice.dto.BetRequest;
import com.slow3586.bettingplatform.api.mainservice.dto.GameDto;
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

    public List<BetDto> getByCurrentUser() {
        return betRepository.findAllByUserId(SecurityUtils.getPrincipalId())
            .stream()
            .map(betMapper::toDto)
            .toList();
    }

    public List<BetDto> getByUser(UUID userId) {
        return betRepository.findAllByUserId(userId)
            .stream()
            .map(betMapper::toDto)
            .toList();
    }

    public UUID make(BetRequest betRequest) {
        final BetDto dto = betMapper.requestToDto(betRequest);
        dto.setUserId(SecurityUtils.getPrincipalId());
        return this.save(dto);
    }

    protected UUID save(BetDto betDto) {
        final BetEntity save = betRepository.save(betMapper.toEntity(betDto));
        return save.getId();
    }

    @KafkaListener(topics = "game")
    protected void gameListener(GameDto gameDto) {
    }
}
