package com.slow3586.bettingplaftorm.api;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PriceGameDto {
    UUID id;
    String instrument;
    LocalDateTime dateStarted;
    LocalDateTime dateFinished;
    boolean isFinished;
}
