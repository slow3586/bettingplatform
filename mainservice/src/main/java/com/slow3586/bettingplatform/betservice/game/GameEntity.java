package com.slow3586.bettingplatform.betservice.game;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Data
@Builder
@Table("game")
public class GameEntity {
    @Id
    UUID id;
    String instrument;
    LocalDateTime startAt;
    LocalDateTime finishAt;
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now(ZoneId.of("UTC"));
    String choice0;
    String choice1;
    String choice2;
    int winningChoice;
    @Builder.Default
    String status = "new";
}
