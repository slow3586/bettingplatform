package com.slow3586.bettingplaftorm.betservice.game;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Table("game")
public class GameEntity {
    @Id
    UUID id;
    String instrument;
    LocalDateTime dateStarted;
    Duration length;
    LocalDateTime dateFinished;
    boolean isFinished;
}
