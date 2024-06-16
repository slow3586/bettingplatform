package com.slow3586.bettingplatform.api.mainservice;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BetDto {
    UUID id;
    UUID gameId;
    UUID userId;
    int typeId;
    String status;
    double amount;
}
