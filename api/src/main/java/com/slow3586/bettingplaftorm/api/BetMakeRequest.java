package com.slow3586.bettingplaftorm.api;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BetMakeRequest {
    UUID userId;
    UUID gameId;
    int betTypeId;
    String value;
}
