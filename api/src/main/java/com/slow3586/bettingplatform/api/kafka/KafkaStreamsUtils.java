package com.slow3586.bettingplatform.api.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.kafka.support.serializer.JsonSerde;

public class KafkaStreamsUtils {
    public static <A> JsonSerde<A> jsonSerde(Class<A> clazz){
        JsonSerde<A> jsonSerde = new JsonSerde<>(clazz);
        jsonSerde.deserializer().addTrustedPackages("*");
        return jsonSerde;
    }

    public static <A> Consumed<String, A> consumed(Class<A> clazz){
        JsonSerde<A> jsonSerde = new JsonSerde<>(clazz);
        jsonSerde.deserializer().addTrustedPackages("*");
        return Consumed.with(Serdes.String(), jsonSerde);
    }

    public static <A> Produced<String, A> produced(Class<A> clazz){
        JsonSerde<A> jsonSerde = new JsonSerde<>(clazz);
        jsonSerde.deserializer().addTrustedPackages("*");
        return Produced.with(Serdes.String(), jsonSerde);
    }
}
