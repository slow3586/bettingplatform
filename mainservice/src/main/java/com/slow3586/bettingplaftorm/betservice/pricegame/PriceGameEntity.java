package com.slow3586.bettingplaftorm.betservice.pricegame;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Table("price_game")
public class PriceGameEntity {
    @Id
    UUID id;
    String instrument;
    LocalDateTime dateStarted;
    LocalDateTime dateFinished;
    boolean isFinished;
}
