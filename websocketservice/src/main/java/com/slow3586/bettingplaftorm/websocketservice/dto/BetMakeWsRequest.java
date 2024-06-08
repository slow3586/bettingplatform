package com.slow3586.bettingplaftorm.websocketservice.dto;

import lombok.Data;

@Data
public class BetMakeWsRequest {
    int betId;
    String value;
}
