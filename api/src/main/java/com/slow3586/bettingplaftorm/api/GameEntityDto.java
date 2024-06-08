package com.slow3586.bettingplaftorm.api;

import lombok.Builder;
import lombok.Data;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class GameEntityDto {
    UUID id;
    LocalDateTime dateStarted;
    Duration length;
    LocalDateTime dateFinished;
    boolean isFinished;
}
