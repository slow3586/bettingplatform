package com.slow3586.bettingplatform.api.mainservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GameTypeDto {
    UUID id;
    String instrument;
    Instant createdAt;
    Instant startAt;
    Instant finishAt;
    String choice0;
    String choice1;
    String choice2;
    boolean isFinished;
    String status;
}
