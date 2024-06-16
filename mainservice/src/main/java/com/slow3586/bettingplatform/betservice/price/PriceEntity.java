package com.slow3586.bettingplatform.betservice.price;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Table("price")
public class PriceEntity {
    @Id
    UUID id;
    Instant time;
    String instrument;
    double value;
}
