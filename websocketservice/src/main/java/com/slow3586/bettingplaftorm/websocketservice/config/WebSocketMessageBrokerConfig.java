package com.slow3586.bettingplaftorm.websocketservice.config;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketMessageBrokerConfig implements WebSocketMessageBrokerConfigurer {
    WebSocketInterceptor webSocketInterceptor;
    @NonFinal
    @Value("${spring.rabbitmq.host}")
    String rabbitHost;
    @NonFinal
    @Value("${spring.rabbitmq.username}")
    String rabbitUsername;
    @NonFinal
    @Value("${spring.rabbitmq.password}")
    String rabbitPw;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes("/app")
            .setUserDestinationPrefix("/user")
            .enableStompBrokerRelay("/queue", "/topic")
            .setRelayHost(rabbitHost)
            .setRelayPort(61613)
            .setClientLogin(rabbitUsername)
            .setSystemLogin(rabbitUsername)
            .setSystemPasscode(rabbitPw)
            .setClientPasscode(rabbitPw);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*")
            .withSockJS();
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(webSocketInterceptor);
    }
}
