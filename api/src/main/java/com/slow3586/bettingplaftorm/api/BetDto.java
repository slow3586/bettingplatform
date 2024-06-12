package com.slow3586.bettingplaftorm.api;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BetDto {
    UUID id;
    UUID gameId;
    UUID userId;
    int typeId;
    String value;
}
