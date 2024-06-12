package com.slow3586.bettingplaftorm.websocketservice.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ConfigurationProperties(prefix = "app.websocket")
@Getter
public class WebSocketProperties {
    String rabbitHost;
    String kafkaBroker;
}
