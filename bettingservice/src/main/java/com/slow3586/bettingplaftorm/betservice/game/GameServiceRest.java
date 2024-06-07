package com.slow3586.bettingplaftorm.betservice.game;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController(value = "game")
@RequiredArgsConstructor
public class GameServiceRest {
    GameService gameService;

    @GetMapping
    public GameEntityDto get(UUID uuid) {
        return gameService.get(uuid);
    }
}
