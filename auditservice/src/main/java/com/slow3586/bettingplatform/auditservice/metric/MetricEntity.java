package com.slow3586.bettingplatform.auditservice.metric;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "metric")
public class MetricEntity {
    @Id
    UUID id;
    Instant time;
    String serviceName;
    Map<String, Double> values;
}
