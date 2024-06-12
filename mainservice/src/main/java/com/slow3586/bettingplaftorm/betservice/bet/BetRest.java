package com.slow3586.bettingplaftorm.betservice.bet;

import com.slow3586.bettingplaftorm.api.BetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController(value = "bet")
@RequiredArgsConstructor
public class BetRest {
    BetService betService;

    @PostMapping
    public UUID make(BetDto betDto) {
        return betService.save(betDto);
    }
}
