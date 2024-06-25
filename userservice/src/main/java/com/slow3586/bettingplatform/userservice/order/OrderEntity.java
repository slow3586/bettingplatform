package com.slow3586.bettingplatform.userservice.order;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Table("order")
public class OrderEntity {
    @Id
    UUID id;
    UUID userId;
    UUID itemId;
    Instant time;
}
