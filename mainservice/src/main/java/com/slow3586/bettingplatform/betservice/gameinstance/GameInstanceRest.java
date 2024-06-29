package com.slow3586.bettingplatform.betservice.gameinstance;

import com.slow3586.bettingplatform.api.mainservice.dto.GameInstanceDto;
import com.slow3586.bettingplatform.api.mainservice.dto.GameInstanceRegisterRequest;
import com.slow3586.bettingplatform.api.mainservice.dto.GameInstanceUpdateRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("game")
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class GameInstanceRest {
    GameInstanceService gameInstanceService;
    KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("/{gameName}")
    public List<GameInstanceDto> getByGameName(@PathVariable String gameName) {
        return gameInstanceService.getCurrent(gameName);
    }

    @PostMapping("register")
    public void register(@RequestBody GameInstanceRegisterRequest gameInstanceRegisterRequest) {
        kafkaTemplate.send("game_instance.register",
            gameInstanceRegisterRequest.getGameName(),
            gameInstanceRegisterRequest);
    }

    @PostMapping("update")
    public void update(@RequestBody GameInstanceUpdateRequest gameInstanceUpdateRequest) {
        kafkaTemplate.send("game_instance.update",
            gameInstanceUpdateRequest.getInstanceName(),
            gameInstanceUpdateRequest);
    }
}
