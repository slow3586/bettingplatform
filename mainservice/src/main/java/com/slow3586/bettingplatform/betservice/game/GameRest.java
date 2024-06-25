package com.slow3586.bettingplatform.betservice.game;

import com.slow3586.bettingplatform.api.mainservice.dto.GameDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("game")
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class GameRest {
    GameService gameService;

    @GetMapping("current")
    public List<GameDto> getCurrent() {
        return gameService.getCurrent();
    }
}
