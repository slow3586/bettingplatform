package com.slow3586.bettingplatform.betservice.game;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Table("game")
public class GameEntity {
    @Id
    UUID id;
    @NonNull
    String instrument;
    @NonNull
    Instant startAt;
    @NonNull
    Instant finishAt;
    @Builder.Default
    @NonNull
    Instant createdAt = Instant.now();
    @NonNull
    String choice0;
    @NonNull
    String choice1;
    String choice2;
    int winningChoice;
    @Builder.Default
    @NonNull
    String status = "new";
}
