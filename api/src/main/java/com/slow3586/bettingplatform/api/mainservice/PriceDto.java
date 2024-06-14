package com.slow3586.bettingplatform.api.mainservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceDto {
    UUID id;
    LocalDateTime time;
    String instrument;
    double value;
}
