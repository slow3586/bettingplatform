package com.slow3586.bettingplatform.betservice.pricegame;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Table("price_game")
public class PriceGameEntity {
    @Id
    UUID id;
    String name;
    double price;
    String priceUrl;
    String pricePattern;
    Instant priceUpdatedAt;
    Instant finishAt;
    boolean finished;
}
