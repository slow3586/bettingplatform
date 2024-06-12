package com.slow3586.bettingplaftorm.websocketservice.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class BetMakeRequest {
    UUID gameId;
    int betTypeId;
    String betValue;
}
