package com.slow3586.bettingplatform.betservice.gametype;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table("game_type")
public class GameTypeEntity {
    @Id
    UUID id;
    String name;
}
