package com.slow3586.bettingplaftorm.websocketservice;

import com.slow3586.bettingplaftorm.api.BetMakeRequest;
import com.slow3586.bettingplaftorm.websocketservice.client.BetServiceClient;
import com.slow3586.bettingplaftorm.websocketservice.dto.BetMakeWsRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.UUID;

@Controller
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public class WebSocketController {
    BetServiceClient betServiceClient;

    @MessageMapping("/bet_make")
    @SendToUser("/queue/feed")
    public UUID send(
        Principal principal,
        Message<BetMakeWsRequest> message
    ) {
        return betServiceClient.make(BetMakeRequest.builder()
            .userId(UUID.fromString(principal.getName()))
            .betTypeId(message.getPayload().getBetId())
            .value(message.getPayload().getValue())
            .build());
    }
}
