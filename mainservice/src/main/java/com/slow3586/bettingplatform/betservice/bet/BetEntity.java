package com.slow3586.bettingplatform.betservice.bet;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Data
@Builder
@Table("bet")
public class BetEntity {
    @Id
    UUID id;
    UUID userId;
    int typeId;
    String status;
    BigInteger amount;
    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now(ZoneId.of("UTC"));
}
