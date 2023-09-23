package com.example.chestGameServer.configs.WS;

import com.example.chestGameServer.Services.UserService;
import com.example.chestGameServer.configs.MVC.HttpHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
@RequiredArgsConstructor

public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final UserService userService;
    public static final String REGISTRY = "/ws";
    public static final String TOPIC_DESTINATION_PREFIX = "/topic/";
    public static final String QUEUE_DESTINATION_PREFIX = "/queue/";
    public static final String APPLICATION_DESTINATION_PREFIX = "/app";
    public static final String USER_DESTINATION_PREFIX = "/user/";

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableStompBrokerRelay(TOPIC_DESTINATION_PREFIX);
    }

    @Bean
    public HttpHandshakeInterceptor httpHandshakeInterceptor() {
        return new HttpHandshakeInterceptor(userService);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(REGISTRY)
                .withSockJS();
    }
}