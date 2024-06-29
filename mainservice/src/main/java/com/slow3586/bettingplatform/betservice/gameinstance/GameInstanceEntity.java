package com.slow3586.bettingplatform.betservice.gameinstance;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Table("game_instance")
public class GameInstanceEntity {
    @Id
    UUID id;
    String name;
    String choice0;
    String choice1;
    String status;
    Instant createdAt;
    Instant startedAt;
    Instant finishedAt;
    Instant updatedAt;
}
