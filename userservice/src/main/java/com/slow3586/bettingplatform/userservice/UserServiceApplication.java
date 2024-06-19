package com.slow3586.bettingplatform.userservice;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties
@ComponentScan(value = {"com.slow3586.bettingplatform.*"})
@EnableScheduling
@EnableWebFlux
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@SecurityScheme(
    name = "BearerAuth",
    scheme = "bearer",
    bearerFormat = "JWT",
    type = SecuritySchemeType.HTTP,
    in = SecuritySchemeIn.HEADER)
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

}
