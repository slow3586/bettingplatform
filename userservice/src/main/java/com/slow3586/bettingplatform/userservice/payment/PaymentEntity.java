package com.slow3586.bettingplatform.userservice.payment;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@Table("payment")
public class PaymentEntity {
    @Id
    UUID id;
    UUID userId;
    Instant time;
    String source;
    double value;
}
