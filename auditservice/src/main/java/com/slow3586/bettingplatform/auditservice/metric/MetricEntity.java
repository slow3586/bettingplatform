package com.slow3586.bettingplatform.auditservice.metric;

import com.slow3586.bettingplatform.api.IMongoUuidEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document("metric")
public class MetricEntity implements IMongoUuidEntity {
    @Id
    UUID id;
    Instant time;
    String serviceName;
    Map<String, Double> values;
}
