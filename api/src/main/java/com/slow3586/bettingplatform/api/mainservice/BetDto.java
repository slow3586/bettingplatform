package com.slow3586.bettingplatform.api.mainservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BetDto {
    UUID id;
    UUID gameId;
    UUID userId;
    int typeId;
    String status;
    double amount;
}
