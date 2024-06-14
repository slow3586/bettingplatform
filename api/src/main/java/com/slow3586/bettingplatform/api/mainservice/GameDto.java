package com.slow3586.bettingplatform.api.mainservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameDto {
    UUID id;
    String instrument;
    LocalDateTime createdAt;
    LocalDateTime startAt;
    LocalDateTime finishAt;
    String choice0;
    String choice1;
    String choice2;
    boolean isFinished;
    String status;
}
