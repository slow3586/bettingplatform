package com.slow3586.bettingplatform.betservice.gamestat;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Table("gamestat")
public class GameStatEntity {
    @Id
    UUID id;
    String gameName;
    String name;
    Instant time;
    double value;
}
