package com.slow3586.bettingplatform.auditservice.trace;

import com.slow3586.bettingplatform.api.auditservice.MetricDto;
import com.slow3586.bettingplatform.api.auditservice.TraceDto;
import com.slow3586.bettingplatform.auditservice.metric.MetricMapper;
import com.slow3586.bettingplatform.auditservice.metric.MetricRepository;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@Slf4j
public class TraceService {
    TraceRepository traceRepository;
    TraceMapper traceMapper;
    MetricRepository metricRepository;
    MetricMapper metricMapper;
    MeterRegistry meterRegistry;

    @Bean
    public KStream<String, TraceDto> traceStream(StreamsBuilder streamsBuilder) {
        final JsonSerde<TraceDto> valueSerde = new JsonSerde<>();
        valueSerde.deserializer().addTrustedPackages("*");

        final KStream<String, TraceDto> stream = streamsBuilder.stream(
            "trace",
            Consumed.with(
                Serdes.String(),
                valueSerde));

        stream.foreach((String k, TraceDto v) ->
            traceRepository.save(traceMapper.toEntity(v)));

        return stream;
    }

    @Bean
    public KStream<String, MetricDto> metricStream(StreamsBuilder streamsBuilder) {
        final JsonSerde<MetricDto> valueSerde = new JsonSerde<>();
        valueSerde.deserializer().addTrustedPackages("*");

        final KStream<String, MetricDto> stream = streamsBuilder.stream(
            "metric",
            Consumed.with(
                Serdes.String(),
                valueSerde));

        stream.foreach((String key, MetricDto dto) ->
            metricRepository.save(metricMapper.toEntity(dto)));

        return stream;
    }
}
