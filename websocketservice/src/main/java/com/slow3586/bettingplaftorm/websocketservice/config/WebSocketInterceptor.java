package com.slow3586.bettingplaftorm.websocketservice.config;

import com.slow3586.bettingplaftorm.api.UserServiceClient;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@FieldDefaults(level = AccessLevel.PROTECTED, makeFinal = true)
@RequiredArgsConstructor
public class WebSocketInterceptor implements ChannelInterceptor {
    UserServiceClient userServiceClient;

    @Override
    public Message<?> preSend(
        @NonNull final Message<?> message,
        @NonNull final MessageChannel channel
    ) {
        final StompHeaderAccessor headerAccessor =
            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (headerAccessor != null
            && StompCommand.CONNECT.equals(headerAccessor.getCommand())
        ) {
            final String userId = Optional.ofNullable(
                    headerAccessor.getNativeHeader("Authorization")
                ).filter(l -> !l.isEmpty())
                .map(s -> s.get(0))
                .filter(s -> s.contains("Bearer "))
                .map(s -> s.substring("Bearer ".length()))
                .map(userServiceClient::token)
                .map(Object::toString)
                .orElseThrow(() -> new IllegalArgumentException("Could not authenticate user"));
            headerAccessor.setUser(() -> userId);
        }

        return message;
    }
}
