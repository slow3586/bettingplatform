package com.slow3586.bettingplatform.betservice.gamestat;

import com.slow3586.bettingplatform.api.mainservice.dto.PriceGameDto;
import com.slow3586.bettingplatform.betservice.gametype.GameTypeRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class GameStatService {
    GameStatRepository gameStatRepository;
    GameTypeRepository gameRepository;
    GameStatMapper gameStatMapper;

    public List<PriceGameDto> getLatest(String gameName) {
        return gameStatRepository.findLatest(gameName)
            .stream()
            .map(gameStatMapper::toDto)
            .toList();
    }
}
