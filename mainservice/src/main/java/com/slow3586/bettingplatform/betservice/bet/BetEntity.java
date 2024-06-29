package com.slow3586.bettingplatform.betservice.bet;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Table("bet")
public class BetEntity {
    @Id
    UUID id;
    String gameName;
    String userName;
    int type;
    String status;
    double amount;
    Instant createdAt;
}
