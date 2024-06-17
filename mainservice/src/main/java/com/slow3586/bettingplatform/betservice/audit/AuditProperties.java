package com.slow3586.bettingplatform.betservice.audit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ConfigurationProperties(prefix = "app.main-service.audit")
@Getter
public class AuditProperties {
}
