package com.slow3586.bettingplaftorm.betservice.bet;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Builder
@Table("bets")
public class BetEntity {
    @Id
    UUID id;
    UUID userId;
    int typeId;
    String value;
}
