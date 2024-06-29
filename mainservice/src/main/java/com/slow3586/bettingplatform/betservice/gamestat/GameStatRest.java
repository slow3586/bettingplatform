package com.slow3586.bettingplatform.betservice.gamestat;

import com.slow3586.bettingplatform.api.mainservice.dto.GameInstanceUpdateRequest;
import com.slow3586.bettingplatform.api.mainservice.dto.PriceGameDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("game_stat")
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
public class GameStatRest {
    GameStatService gameStatService;
    KafkaTemplate<String, Object> kafkaTemplate;
    ReplyingKafkaTemplate<String, Object, Object> replyingKafkaTemplate;

    @GetMapping("/{uuid}")
    public List<PriceGameDto> getLatest(@PathVariable("uuid") UUID gameId) {
        return gameStatService.getLatest(gameId);
    }

    @PostMapping("update")
    public void update(@RequestBody GameInstanceUpdateRequest gameInstanceUpdateRequest) {
        replyingKafkaTemplate.sendAndReceive()
        kafkaTemplate.send("game_stat.update",
            gameInstanceUpdateRequest.getGameName(),
            gameInstanceUpdateRequest);
    }
}
