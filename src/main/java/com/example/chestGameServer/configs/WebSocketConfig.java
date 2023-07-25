package com.example.chestGameServer.configs;

import com.example.chestGameServer.Models.User.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
//@Order(Ordered.HIGHEST_PRECEDENCE+99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    public static final String REGISTRY="/ws";
    public static final String TOPIC_DESTINATION_PREFIX="/topic/";
    public static final String QUEUE_DESTINATION_PREFIX="/queue/";
    public static final String APPLICATION_DESTINATION_PREFIX="/app";
    public static final String USER_DESTINATION_PREFIX="/user/";
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(TOPIC_DESTINATION_PREFIX);
        config.setApplicationDestinationPrefixes(APPLICATION_DESTINATION_PREFIX);
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(REGISTRY)
                //.addInterceptors(new HttpHandshakeInterceptor())
                .withSockJS();
    }

}