package com.slow3586.bettingplatform.betservice.gametype;

import com.slow3586.bettingplatform.api.mainservice.dto.GameTypeDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("game")
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class GameTypeRest {
    GameTypeService gameTypeService;
    KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("/{name}")
    public GameTypeDto getByName(@PathVariable String name) {
        return gameTypeService.getByName(name);
    }
}
