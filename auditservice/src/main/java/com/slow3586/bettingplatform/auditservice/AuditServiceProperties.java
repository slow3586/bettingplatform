package com.slow3586.bettingplatform.auditservice;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@ConfigurationProperties(prefix = "app.audit-service")
@Getter
public class AuditServiceProperties {
    String kafkaBroker;
}
