package com.slow3586.bettingplaftorm.betservice.bet;

import com.slow3586.bettingplaftorm.api.BetMakeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController(value = "bet")
@RequiredArgsConstructor
public class BetServiceRest {
    BetService betService;

    @PutMapping
    public UUID make(BetMakeRequest betMakeRequest) {
        return betService.make(betMakeRequest);
    }
}
