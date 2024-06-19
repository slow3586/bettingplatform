package com.slow3586.bettingplatform.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@SpringBootApplication
@ComponentScan(value = {"com.slow3586.bettingplatform.*"})
@EnableKafka
@EnableAspectJAutoProxy
@EnableWebSecurity
public class FrontendApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrontendApplication.class, args);
    }

    @Bean
    public DefaultSecurityFilterChain securityWebFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(c -> c.anyRequest().permitAll())
            .csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .securityContext(c -> c.securityContextRepository(
                new HttpSessionSecurityContextRepository()))
            .build();
    }
}
