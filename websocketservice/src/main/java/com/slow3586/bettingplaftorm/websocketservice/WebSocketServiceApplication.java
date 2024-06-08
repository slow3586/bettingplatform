package com.slow3586.bettingplaftorm.websocketservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableCaching
@EnableWebSecurity
@EnableMethodSecurity
@EnableScheduling
public class WebSocketServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebSocketServiceApplication.class, args);
	}

}
