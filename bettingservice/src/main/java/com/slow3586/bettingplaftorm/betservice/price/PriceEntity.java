package com.slow3586.bettingplaftorm.betservice.price;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Table("prices")
public class PriceEntity {
    @Id
    UUID id;
    LocalDateTime time;
    String instrument;
    double value;
}
