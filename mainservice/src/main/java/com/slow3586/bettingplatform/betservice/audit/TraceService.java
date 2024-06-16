package com.slow3586.bettingplatform.betservice.audit;

import com.slow3586.bettingplatform.api.TraceDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KeyValueMapper;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.processor.WallclockTimestampExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@EnableKafkaStreams
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@AuditDisabled
public class TraceService {
    static KeyValueMapper<String, String, String> BY_VALUE = (k, v) -> v;
    static TimeWindows ONE_SECOND = TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(1));

    TraceRepository traceRepository;
    TraceMapper traceMapper;

    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
    public KafkaStreamsConfiguration kStreamsConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "awoeirhj");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG, WallclockTimestampExtractor.class.getName());
        return new KafkaStreamsConfiguration(props);
    }

    @Bean
    public KStream<String, TraceDto> aweyhaw(StreamsBuilder streamsBuilder) {
        final JsonSerde<TraceDto> valueSerde = new JsonSerde<>();
        valueSerde.deserializer().addTrustedPackages("*");

        final KStream<String, TraceDto> stream = streamsBuilder.stream(
            "trace",
            Consumed.with(
                Serdes.String(),
                valueSerde));

        stream.foreach((k, v) ->
            traceRepository.save(traceMapper.toEntity(v)));

        return stream;
    }
}
