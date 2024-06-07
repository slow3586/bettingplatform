package com.slow3586.bettingplaftorm.betservice.price;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ConfigurationProperties(prefix = "app.price")
@Getter
public class PriceServiceProperties {
    String requestInstrument;
    String requestPath;
    String responseRegex;
    String requestDateFormat;
}
