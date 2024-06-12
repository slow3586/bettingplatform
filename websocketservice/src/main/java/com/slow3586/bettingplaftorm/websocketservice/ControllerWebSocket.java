package com.slow3586.bettingplaftorm.websocketservice;

import com.slow3586.bettingplaftorm.api.BetDto;
import com.slow3586.bettingplaftorm.api.BetServiceClient;
import com.slow3586.bettingplaftorm.api.PriceDto;
import com.slow3586.bettingplaftorm.api.PriceServiceClient;
import com.slow3586.bettingplaftorm.websocketservice.dto.BetMakeRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public class ControllerWebSocket {
    PriceServiceClient priceServiceClient;
    BetServiceClient betServiceClient;

    @MessageMapping("/get_price_latest")
    @SendToUser
    public List<PriceDto> getPriceLatest() {
        return priceServiceClient.getLatest();
    }

    @MessageMapping("/make_bet")
    @SendToUser
    public UUID makeBet(
        Principal principal,
        Message<BetMakeRequest> message
    ) {
        final BetMakeRequest payload = message.getPayload();
        return betServiceClient.make(
            BetDto.builder()
                .userId(UUID.fromString(principal.getName()))
                .gameId(payload.getGameId())
                .typeId(payload.getBetTypeId())
                .value(payload.getBetValue())
                .build());
    }

    @MessageMapping("/new_bet")
    @SendToUser
    public UUID make(
        Principal principal,
        Message<BetMakeRequest> message
    ) {
        final BetMakeRequest payload = message.getPayload();
        return betServiceClient.make(
            BetDto.builder()
                .userId(UUID.fromString(principal.getName()))
                .gameId(payload.getGameId())
                .typeId(payload.getBetTypeId())
                .value(payload.getBetValue())
                .build());
    }
}
