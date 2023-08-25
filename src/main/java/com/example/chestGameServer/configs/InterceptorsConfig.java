package com.example.chestGameServer.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
@Configuration
@RequiredArgsConstructor
public class InterceptorsConfig implements WebSocketMessageBrokerConfigurer {
    private final AuthChannelInterceptorAdapter authInterceptor;
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(authInterceptor);
    }
}
